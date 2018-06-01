# 测试脚本及数据的自动化生成工具

该项目主要是通过分析给定格式的接口规范文档(XML格式)来进行测试脚本(步骤)步骤以及测试数据的自动化生成

**测试脚本及数据的自动化生成工具**主要的实现语言是`Java`

# 输入文件格式
输入文件要求为一个严格按照自定义的<a src="https://github.com/aalm1233/AutoSystem-Test/tree/master/resource/schema">xmlschema</a>格式编写的命名空间为空的xml文件，示例如下：
```
<?xml version="1.0" encoding="UTF-8"?>
<resources base="./service/seller" id="IAddTravelDateService" name="IAddTravelDateService" xmlns="http://www.w3school.com.cn" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3school.com.cn schema/XMLSchema11_24.xsd ">
	<add>
		<resource name="Add_TravelDate" path="addTravelDate" id="addTravelDate">
			<request>
				<param name="travelDate" type="Traveldate" attribute="Traveldate">
				</param>
			</request>
			<response dataType="">
			</response>
		</resource>
	</add>
	<delete>
		<resource name="Delete_TravelDate" path="deleteTravelDate" id="deleteTravelDate">
			<request>
				<param name="travelDate" type="Traveldate" attribute="Traveldate" location="Traveldate">
				</param>
			</request>
			<response dataType="">
			</response>
		</resource>
	</delete>
	<update>
		<resource name="Update_TravelDate" path="modifyTravelDate" id="modifyTravelDate">
			<request>
				<param name="travelDate" type="Traveldate" attribute="Traveldate">
				</param>
			</request>
			<response dataType=""></response>
		</resource>
	</update>
	<find>
		<resource name="Find_TravelDate_ById" path="findTravelDateById" id="findTravelDateById">
			<request>
				<param name="id" type="int" attribute="Traveldate.idtravelDate">
				</param>
			</request>
			<response dataType="Traveldate">
			<param name="travelDate" type="Traveldate" attribute="Traveldate">
				</param>
			</response>
		</resource>
	</find>
</resources>

```