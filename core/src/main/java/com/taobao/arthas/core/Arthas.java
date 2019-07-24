package com.taobao.arthas.core;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.taobao.arthas.common.AnsiLog;
import com.taobao.arthas.common.JavaVersionUtils;
import com.taobao.arthas.core.config.Configure;
import com.taobao.middleware.cli.CLI;
import com.taobao.middleware.cli.CLIs;
import com.taobao.middleware.cli.CommandLine;
import com.taobao.middleware.cli.Option;
import com.taobao.middleware.cli.TypedOption;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Properties;

/**
 * Arthas启动器
 */
public class Arthas {
	
	//telnet server 服务端监听端口
    private static final String DEFAULT_TELNET_PORT = "3658";
    
    //http websocket server 服务端监听端口
    private static final String DEFAULT_HTTP_PORT = "8563";

    private Arthas(String[] args) throws Exception {
        attachAgent(parse(args));
    }

    /*
     * 参数：
     *  // "${JAVA_HOME}"/bin/java \
        // ${opts} \
        // -jar "${arthas_lib_dir}/arthas-core.jar" \
        // -pid ${TARGET_PID} \
        // -target-ip ${TARGET_IP} \
        // -telnet-port ${TELNET_PORT} \
        // -http-port ${HTTP_PORT} \
        // -core "${arthas_lib_dir}/arthas-core.jar" \
        // -agent "${arthas_lib_dir}/arthas-agent.jar"
     */
    private Configure parse(String[] args) {
    	//定义一个Cli，用于把命令行参数转化成CommandLine对象
        Option pid = new TypedOption<Integer>().setType(Integer.class).setShortName("pid").setRequired(true);
        Option core = new TypedOption<String>().setType(String.class).setShortName("core").setRequired(true);
        Option agent = new TypedOption<String>().setType(String.class).setShortName("agent").setRequired(true);
        
        //BootStrap启动时会传默认值Bootstrap.DEFAULT_TARGET_IP
        Option target = new TypedOption<String>().setType(String.class).setShortName("target-ip");
        
        //Bootstrap启动时就会传默认值Bootstrap.DEFAULT_TELNET_PORT，所以这里set的DefaultValue不会用到
        Option telnetPort = new TypedOption<Integer>().setType(Integer.class)
                .setShortName("telnet-port").setDefaultValue(DEFAULT_TELNET_PORT);
        
        //Bootstrap启动时就会传默认值Bootstrap.DEFAULT_HTTP_PORT，所以这里set的DefaultValue不会用到
        Option httpPort = new TypedOption<Integer>().setType(Integer.class)
                .setShortName("http-port").setDefaultValue(DEFAULT_HTTP_PORT);
        
        Option sessionTimeout = new TypedOption<Integer>().setType(Integer.class)
                        .setShortName("session-timeout").setDefaultValue("" + Configure.DEFAULT_SESSION_TIMEOUT_SECONDS);
        
        //app client id
        Option appClientIdOption = new TypedOption<Long>().setType(Long.class).setShortName("app-client-id").setRequired(true);
        
        CLI cli = CLIs.create("arthas").addOption(pid).addOption(core).addOption(agent).addOption(target)
                .addOption(telnetPort).addOption(httpPort).addOption(sessionTimeout).addOption(appClientIdOption);
        
        //zjd 根据Cli的定义，把命令行参数转化成CommandLine
        CommandLine commandLine = cli.parse(Arrays.asList(args));
        
        //zjd 存放命令行传递的参数。
        //【优化：这里可以在Configure类定义上加上Cli的注解，然后使用CLIConfigurator.inject(Configure, command);直接把参数set到Config对象中。】
        Configure configure = new Configure();
        configure.setJavaPid((Integer) commandLine.getOptionValue("pid"));
        configure.setArthasAgent((String) commandLine.getOptionValue("agent"));
        configure.setArthasCore((String) commandLine.getOptionValue("core"));
        configure.setSessionTimeout((Integer)commandLine.getOptionValue("session-timeout"));
        if (commandLine.getOptionValue("target-ip") == null) {
            throw new IllegalStateException("as.sh is too old to support web console, " +
                    "please run the following command to upgrade to latest version:" +
                    "\ncurl -sLk https://alibaba.github.io/arthas/install.sh | sh");
        }
        configure.setIp((String) commandLine.getOptionValue("target-ip"));
        configure.setTelnetPort((Integer) commandLine.getOptionValue("telnet-port"));
        configure.setHttpPort((Integer) commandLine.getOptionValue("http-port"));
        configure.setAppClientId((Long)commandLine.getOptionValue("app-client-id"));
        return configure;
    }

    private void attachAgent(Configure configure) throws Exception {
        VirtualMachineDescriptor virtualMachineDescriptor = null;
        for (VirtualMachineDescriptor descriptor : VirtualMachine.list()) {
            String pid = descriptor.id();
            if (pid.equals(Integer.toString(configure.getJavaPid()))) {
                virtualMachineDescriptor = descriptor;
            }
        }
        
        //zjd 目标jvm进程
        VirtualMachine virtualMachine = null;
        try {
            if (null == virtualMachineDescriptor) { // 使用 attach(String pid) 这种方式
                virtualMachine = VirtualMachine.attach("" + configure.getJavaPid());
            } else {
                virtualMachine = VirtualMachine.attach(virtualMachineDescriptor);
            }

            Properties targetSystemProperties = virtualMachine.getSystemProperties();
            String targetJavaVersion = JavaVersionUtils.javaVersionStr(targetSystemProperties);
            String currentJavaVersion = JavaVersionUtils.javaVersionStr();
            
            //zjd 目标jvm进程的jdk版本与启动arthas时使用的jdk版本 比较
            if (targetJavaVersion != null && currentJavaVersion != null) {
                if (!targetJavaVersion.equals(currentJavaVersion)) {
                    AnsiLog.warn("Current VM java version: {} do not match target VM java version: {}, attach may fail.",
                                    currentJavaVersion, targetJavaVersion);
                    AnsiLog.warn("Target VM JAVA_HOME is {}, arthas-boot JAVA_HOME is {}, try to set the same JAVA_HOME.",
                                    targetSystemProperties.getProperty("java.home"), System.getProperty("java.home"));
                }
            }

            String arthasAgentPath = configure.getArthasAgent();
            //convert jar path to unicode string
            configure.setArthasAgent(encodeArg(arthasAgentPath));
            configure.setArthasCore(encodeArg(configure.getArthasCore()));
            
            //zjd 加载agent的jar包，attach到目标进程，同时把config信息传递到agent的agentMain方法
            //configure.toString()方法会用“；”分隔option，用”=“连接key=value
            virtualMachine.loadAgent(arthasAgentPath,
                    configure.getArthasCore() + ";" + configure.toString());
        } finally {
            if (null != virtualMachine) {
                virtualMachine.detach();
            }
        }
    }

    private static String encodeArg(String arg) {
        try {
            return URLEncoder.encode(arg, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return arg;
        }
    }

    public static void main(String[] args) {
        try {
            new Arthas(args);
        } catch (Throwable t) {
            AnsiLog.error("Start arthas failed, exception stack trace: ");
            t.printStackTrace();
            System.exit(-1);
        }
    }
}
