<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item>
					<el-input v-model="filters.name" placeholder="启动命令"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" >查询</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<template>
			<el-table :data="appRegistList" highlight-current-row v-loading="loading" style="width: 100%;">
				<el-table-column prop="appIp" label="服务器IP" width="150">
				</el-table-column>
				<el-table-column prop="appStartCmd" label="启动命令" width="600">
				</el-table-column>
				<el-table-column prop="isAttached" label="是否attached" width="150">
				</el-table-column>
				<el-table-column prop="pid" label="进程id" min-width="100">
				</el-table-column>
			</el-table>
		</template>

	</section>
</template>
<script>
	//import { getAppRegistList } from '../../api/api';
	import axios from 'axios';

	//import NProgress from 'nprogress'
	export default {
		data() {
			return {
				filters: {
					name: ''
				},
				loading: false,
				appRegistList: [
				]
			}
		},
		methods: {
			//性别显示转换
			formatSex: function (row, column) {
				return row.sex == 1 ? '男' : row.sex == 0 ? '女' : '未知';
			},
			//获取app client list
			getAppClientList(){
				axios.get('http://10.206.88.235:8080/manage/getAppClientList',{})
				.then(response=>{
					console.log(response);
					this.loading = false;
					this.appRegistList = response.data.result;
				})
				.catch(function (error) {
					console.log(error);
				});
			}
		},
		mounted() {
			this.loading = true;
			this.getAppClientList();
		}
	};

</script>

<style scoped>

</style>