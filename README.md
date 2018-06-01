# 测试脚本及数据的自动化生成工具

该项目主要是通过分析给定格式的接口规范文档(XML格式)来进行测试脚本(步骤)步骤以及测试数据的自动化生成.

**测试脚本及数据的自动化生成工具**主要的实现语言是`Java`.

# 输入文件格式

输入文件要求为一个严格按照自定义的`xmlschema`格式编写的命名空间为空的xml文件，示例如下：

```xml
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

* Schema定义 ：

https://github.com/aalm1233/AutoSystem-Test/blob/master/resource/schema/XMLSchema11_24.xsd

# 如何运行

## 直接运行包

在命令行中到所在目录直接运行`App.jar`:

```cmd
Java -jar App.jar resoucepath
```

其中`resoucepath`指的是所有输入文件所在文件夹的绝对路径.

## 在eclipse中运行

该项目是一个Maven项目，在导入该项目时需要选择导入maven项目。如果提示缺少依赖的包请检查java版本是否正确，以及在`resource`文件夹下的`Xegex`文件夹及`poi`文件夹中的包是否添加进了path.

确认项目不报错之后找到`nwpu.autosysteamtest`包下的`App.java`，配置好命令行参数`resoucepath`(意义同上)然后运行.

# 项目构成

该项目由如下几个部分构成：

* 接口规范文件解析模块
* 测试模型生成模块
* 测试模式及测试序列(脚本)生成模块
* 测试脚本生成模块

## 接口规范文件解析模块

该部分的主要的作用是XML格式的解析接口规范文档，并将其抽象为`nwpu.autosysteamtest.entity`对应的实体存入内存,方便之后的使用.

定义的实体如下：

* Service
* Operation
* RequestParam
* RequestElement
* ResponseParam
* ResponseElement

### Service

一个Service对象对应一个输入文件.

<b>以下是该类的参数介绍</b>：

| 参数名 | 类型  | 说明 |
| - | :-: | :- |
| name | String | 服务名称 |
| id | String | 服务的标识(唯一) |
| base | String | 域名 |
| adds | ArrayList<Operation> | 存放所有的Add类型操作接口 |
| updates | ArrayList<Operation> | 存放所有的Update类型操作接口 |
| deletes | ArrayList<Operation> | 存放所有的Delete类型操作接口 |
| finds | ArrayList<Operation> | 存放所有的Find类型操作接口 |

<b>以下是该类的方法介绍(忽略get以及set方法)</b>：

| 方法 | 说明 | 返回类型 |
| - | :- | :-: |
| Service(String name, String id, String base) | 构造函数 | 无 |
| searchAllOperationById(String id) | 通过操作接口id查找到对应的接口 | Operation |

###  Operation

一个Operation对象对应一个操作接口.

<b>以下是该类的参数介绍</b>：

| 参数名 | 类型  | 说明 |
| - | :-: | :- |
| name | String | 接口(方法)名称 |
| id | String | 接口的标识(唯一) |
| path | String | 接口路径 |
| type | String | 接口的操作类型(四种操作类型的一种) |
| dependencys | ArrayList<Operation> | 该接口操作所关联的所有其他服务的操作接口(前置操作) |
| requestParams | ArrayList<RequestParam> | 存放该接口所需的所有输入参数 |
| response | String | 返回参数的格式(如JSON) |
| responseParams | ArrayList<ResponseParam> | 存放返回的所有参数 |

<b>以下是该类的方法介绍(忽略get、set以及toString方法)</b>：

| 方法 | 说明 | 返回类型 |
| - | :- | :-: |
| equals(Operation obj) | 比较两个接口操作是否为同一个 |  boolean |
| addDependency(Operation dependency) | 为该接口添加一个关联接口 | 无 |

### RequestParam

一个RequestParam对象对应一个输入参数.

<b>以下是该类的参数介绍</b>：

| 参数名 | 类型  | 说明 |
| - | :-: | :- |
| name | String | 参数名称 |
| attribute | String | 参数对应的实体属性名称以匹配生成的数据 |
| type | String | 参数类型 |
| location | String | 标记该参数是否需要生成，若值为`false`则需要生成，若依赖于向上的接口操作则需要填写对应的实体属性 |
| constraints | ArrayList<String> | 存放该参数的约束，字符串的形式为 `约束类型：约束值` |
| elements | ArrayList<RequestElement> |  当输入参数是一个对象时，存放该对象所需的所有输入参数 |

<b>该对象仅有get和set方法,不做介绍</b>
