package com.taobao.arthas.agent;

import java.arthas.Spy;
import java.io.*;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.jar.JarFile;

/**
 * 代理启动类
 *
 * @author vlinux on 15/5/19.
 */
public class AgentBootstrap {

    private static final String ADVICEWEAVER = "com.taobao.arthas.core.advisor.AdviceWeaver";
    private static final String ON_BEFORE = "methodOnBegin";
    private static final String ON_RETURN = "methodOnReturnEnd";
    private static final String ON_THROWS = "methodOnThrowingEnd";
    private static final String BEFORE_INVOKE = "methodOnInvokeBeforeTracing";
    private static final String AFTER_INVOKE = "methodOnInvokeAfterTracing";
    private static final String THROW_INVOKE = "methodOnInvokeThrowTracing";
    private static final String RESET = "resetArthasClassLoader";
    private static final String ARTHAS_SPY_JAR = "arthas-spy.jar";
    private static final String ARTHAS_CONFIGURE = "com.taobao.arthas.core.config.Configure";
    private static final String ARTHAS_BOOTSTRAP = "com.taobao.arthas.core.server.ArthasBootstrap";
    private static final String TO_CONFIGURE = "toConfigure";
    private static final String GET_JAVA_PID = "getJavaPid";
    private static final String GET_INSTANCE = "getInstance";
    private static final String IS_BIND = "isBind";
    private static final String BIND = "bind";

    private static PrintStream ps = System.err;
    static {
        try {
            File log = new File(System.getProperty("user.home") + File.separator + "logs" + File.separator
                    + "arthas" + File.separator + "arthas.log");
            if (!log.exists()) {
                log.getParentFile().mkdirs();
                log.createNewFile();
            }
            ps = new PrintStream(new FileOutputStream(log, true));
        } catch (Throwable t) {
            t.printStackTrace(ps);
        }
    }

    // 全局持有classloader用于隔离 Arthas 实现
    private static volatile ClassLoader arthasClassLoader;

    public static void premain(String args, Instrumentation inst) {
        main(args, inst);
    }

    public static void agentmain(String args, Instrumentation inst) {
        main(args, inst);
    }

    /**
     * 让下次再次启动时有机会重新加载
     */
    public synchronized static void resetArthasClassLoader() {
        arthasClassLoader = null;
    }

    private static ClassLoader getClassLoader(Instrumentation inst, File spyJarFile, File agentJarFile) throws Throwable {
        // 1, 将Spy添加到BootstrapClassLoader。
        inst.appendToBootstrapClassLoaderSearch(new JarFile(spyJarFile));

        // 2, 构造自定义的类加载器，尽量减少Arthas对现有工程的侵蚀.
        // 为什么不把spy的类也加载到这个classLoader？是因为Spy中的编织类必须跟目标类在同一个classLoader里？
        return loadOrDefineClassLoader(agentJarFile);
    }

    private static ClassLoader loadOrDefineClassLoader(File agentJar) throws Throwable {
        if (arthasClassLoader == null) {
            arthasClassLoader = new ArthasClassloader(new URL[]{agentJar.toURI().toURL()});
        }
        return arthasClassLoader;
    }
    
    /**
     * 初始化Spy类的配置
    * @Description 
    * @param 
    * @return 
    * @throws 
    * @author: zhaojindong  @date: 10 May 2019 10:11:46
     */
    private static void initSpy(ClassLoader classLoader) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> adviceWeaverClass = classLoader.loadClass(ADVICEWEAVER);	//加载编织类AdviceWeaver
        Method onBefore = adviceWeaverClass.getMethod(ON_BEFORE, int.class, ClassLoader.class, String.class,
                String.class, String.class, Object.class, Object[].class);
        Method onReturn = adviceWeaverClass.getMethod(ON_RETURN, Object.class);
        Method onThrows = adviceWeaverClass.getMethod(ON_THROWS, Throwable.class);
        Method beforeInvoke = adviceWeaverClass.getMethod(BEFORE_INVOKE, int.class, String.class, String.class, String.class);
        Method afterInvoke = adviceWeaverClass.getMethod(AFTER_INVOKE, int.class, String.class, String.class, String.class);
        Method throwInvoke = adviceWeaverClass.getMethod(THROW_INVOKE, int.class, String.class, String.class, String.class);
        Method reset = AgentBootstrap.class.getMethod(RESET);
        Spy.initForAgentLauncher(classLoader, onBefore, onReturn, onThrows, beforeInvoke, afterInvoke, throwInvoke, reset);
    }
    
    /**
     * 在Arthas.main()中发起attach时，参数使用“；”分隔option，用”=“连接key=value
    * @Description 
    * @param 
    * @return 
    * @throws 
    * @author: zhaojindong  @date: 10 May 2019 10:00:20
     */
    private static synchronized void main(String args, final Instrumentation inst) {
        try {
            ps.println("Arthas server agent start...");
            ps.println("agentmain() received params:" + args);
            // 传递的args参数分两个部分:agentJar路径和agentArgs, 分别是Agent的JAR包路径和期望传递到服务端的参数
            args = decodeArg(args);
            int index = args.indexOf(';');
            String agentJar = args.substring(0, index);
            final String agentArgs = args.substring(index);

            //zjd -agent "${arthas_lib_dir}/arthas-agent.jar"
            File agentJarFile = new File(agentJar);
            if (!agentJarFile.exists()) {
                ps.println("Agent jar file does not exist: " + agentJarFile);
                return;
            }
            
            //zjd 通过agent.jar所在路径去找spy.jar
            File spyJarFile = new File(agentJarFile.getParentFile(), ARTHAS_SPY_JAR);
            if (!spyJarFile.exists()) {
                ps.println("Spy jar file does not exist: " + spyJarFile);
                return;
            }

            /**
             * 把Spy.jar里的类放入BootstrapClassLoader的搜索路径
             * 创建agent自己的classLoader，加载agent.jar。
             * 在Enhancer.isSelf()方法中会通过匹配出来的类的classLoader是不是Arthas的classLoader来判断匹配的类是不是Arthas的类，这样进行过滤。
             * Use a dedicated thread to run the binding logic to prevent possible memory leak. #195
             */
            final ClassLoader agentLoader = getClassLoader(inst, spyJarFile, agentJarFile);
            
            //初始化Spy类的配置
            initSpy(agentLoader);

            Thread bindingThread = new Thread() {
                @Override
                public void run() {
                    try {
                        bind(inst, agentLoader, agentArgs);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace(ps);
                    }
                }
            };

            bindingThread.setName("arthas-binding-thread");
            bindingThread.start();
            bindingThread.join();
        } catch (Throwable t) {
            t.printStackTrace(ps);
            try {
                if (ps != System.err) {
                    ps.close();
                }
            } catch (Throwable tt) {
                // ignore
            }
            throw new RuntimeException(t);
        }
    }

    private static void bind(Instrumentation inst, ClassLoader agentLoader, String args) throws Throwable {
        /**
         * Arthas.java发起attach是就是把Configure对象字符串序列化后变成了这里的args，
         * 所以现在又把args反序列化成Configure对象
         * <pre>
         * Configure configure = Configure.toConfigure(args);
         * int javaPid = configure.getJavaPid();
         * ArthasBootstrap bootstrap = ArthasBootstrap.getInstance(javaPid, inst);
         * </pre>
         */
        Class<?> classOfConfigure = agentLoader.loadClass(ARTHAS_CONFIGURE);
        Object configure = classOfConfigure.getMethod(TO_CONFIGURE, String.class).invoke(null, args);
        
        int javaPid = (Integer) classOfConfigure.getMethod(GET_JAVA_PID).invoke(configure);
        
        /**
         * 调用ArthasBootstrap.getInstance()，获取到ArthasBootstrap的单例对象，判断是否已经有线程bind过这个javaPid
         */
        Class<?> bootstrapClass = agentLoader.loadClass(ARTHAS_BOOTSTRAP);
        Object bootstrap = bootstrapClass.getMethod(GET_INSTANCE, int.class, Instrumentation.class).invoke(null, javaPid, inst);
        
        boolean isBind = (Boolean) bootstrapClass.getMethod(IS_BIND).invoke(bootstrap);
        if (!isBind) {
            try {
                ps.println("Arthas start to bind...");
                //未bind，则调用ArthasBootstrap.bind()
                bootstrapClass.getMethod(BIND, classOfConfigure).invoke(bootstrap, configure);
                ps.println("Arthas server bind success.");
                return;
            } catch (Exception e) {
                ps.println("Arthas server port binding failed! Please check $HOME/logs/arthas/arthas.log for more details.");
                throw e;
            }
        }
        ps.println("Arthas server already bind.");
    }

    private static String decodeArg(String arg) {
        try {
            return URLDecoder.decode(arg, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return arg;
        }
    }
}
