<template>
	<div>
		<NewRichText :ref="'myQuillEditor'"
		                v-model="details"
		                @inputChange="onEditorChange($event)">
		</NewRichText>
		<div style="margin-top:30px;">
			<el-button type="primary" @click="save">保存</el-button>
		</div>
		
	</div>
</template>

<script>
	import { getfinancialStatements,SetFinancialStatistics,setFinancialStatistics } from "@/api/statistics";
	import RichText from './components/RichText';
	import NewRichText from './components/NewRichText';
	export default{
		name:"UpFinancialStatement",
		components: { RichText,NewRichText },
		data() {
			return {
				content:'',
				 details:'',
				idcontent:'',
				form: {
				    announceDesc: ''
				}
			}
		},
	  created() {
		  this.getInfo()
		},
		methods:{
			//将已有的数据回显到富文本框中
			  getInfo(){
			   //假设调用接口返回值为res.data
			   //解析成json格式
			   getfinancialStatements({}).then((res)=>{
				   console.log(res,'成功了嘛')
					// this.details = res.data
					//如果页面中调用多个富文本框,则ref动态
					let r = 'myQuillEditor'
					 if (this.$refs[r]) {
						this.details = JSON.parse(res.data.contentdata)
					   this.$refs[r].quill.setContents(this.details)
					 }
				// console.log(this.form.announceDesc,'成功了嘛？')
			   })
				
			
				//如是单个页面调用方式如下
				//this.$refs.myQuillEditor.quill.setContents(this.details)
			  },
			  onEditorChange(val){
			   //富文本框返回的数据格式为
			   //{"ops":[{"insert":"SSSSSSSSSSSSSSSSSSSSSSSSS\n"}]}
				this.details = val
			  },

			change (val) {
			   // console.log('val: ', val)
			   this.form.announceDesc = val
			},
			save() {
				console.log(this.details,'查看结构')
				let htmldata = JSON.stringify(this.details.htmldata)
				let details = JSON.stringify(this.details.data)
				if(this.details){
					console.log(details,'数据')
					// let datali = {content:htmldata,contentdata:details}
					SetFinancialStatistics({content:this.details.htmldata,contentdata:details}).then((res)=>{
						if(res.success==true){
						  this.$message({
							  message: '保存成功',
							  type: 'success'
							});
						}else{
							 this.$message.error('保存失败');
						}
					})
				}
			},
		}
	}
</script>

<style>
</style>