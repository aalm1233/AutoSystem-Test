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
* 测试数据生成模块

## 接口规范文件解析模块

该部分的主要的作用是XML格式的解析接口规范文档，并将其抽象为`nwpu.autosysteamtest.entity`对应的实体存入内存,方便之后的使用.

定义的实体如下：

* Service
* Operation
* RequestParam
* RequestElement
* ResponseParam
* ResponseElement

所使用的类如下：

* DocumentPrepcessing
* ParameterConstrain
* ElementConstrain


### Service实体

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

###  Operation实体

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

### RequestParam实体

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

### RequestElement实体

一个RequestElement对象对应一个Param的一个属性的输入.

<b>以下是该类的参数介绍</b>：

| 参数名 | 类型  | 说明 |
| - | :-: | :- |
| name | String | 参数名称 |
| attribute | String | 参数对应的实体属性名称以匹配生成的数据 |
| type | String | 参数类型 |
| level | int | 该参数所在层级 |
| location | String | 标记该参数是否需要生成，若值为`false`则需要生成，若依赖于向上的接口操作则需要填写对应的实体属性 |
| constraints | ArrayList<String> | 存放该参数的约束，字符串的形式为 `约束类型：约束值` |
| elements | ArrayList<RequestElement> |  当输入参数是一个对象时，存放该对象所需的所有输入参数 |

<b>以下是该类的方法介绍(忽略get、set以及toString方法)</b>：

| 方法 | 说明 | 返回类型 |
| - | :- | :-: |
| isObject() | 判断该属性是否是一个对象，是则返回true | boolean |

### ResponseParam实体

一个ResponseParam对象对应一个返回参数.

<b>以下是该类的参数介绍</b>：

| 参数名 | 类型  | 说明 |
| - | :-: | :- |
| name | String | 参数名称 |
| attribute | String | 参数对应的实体属性名称以匹配生成的数据 |
| elements | ArrayList<RequestElement> |  当返回参数是一个对象时，存放该对象所有的属性参数 |

<b>该对象仅有get和set方法,不做介绍</b>

### ResponseElement实体

一个ResponseElement对象对应一个Param的一个属性.

<b>以下是该类的参数介绍</b>：

| 参数名 | 类型  | 说明 |
| - | :-: | :- |
| name | String | 参数名称 |
| attribute | String | 参数对应的实体属性名称以匹配生成的数据 |
| type | String | 参数类型 |
| level | int | 该参数所在层级 |
| elements | ArrayList<ResponseElement> |  当输入参数是一个对象时，存放该对象所需的所有输入参数 |

<b>该对象仅有get和set方法,不做介绍</b>

### DocumentPrepcessing

该类是用于解析XML文件并存放所有服务信息，由于采用了多线程的技术故为了线程安全该类采用单例模式.

解析xml采用了`DOM`技术.

<b>该类提供的方法如下</b>：

| 方法 | 说明 | 返回类型 |
| - | :- | :-: |
| searchServiceById(String id) | 通过服务的id去查找对应的Service | Service |
| getInstance() |  获取对象 | DocumentPrepcessing |
| getInstance(File[] fileSet) | 获取对象 | DocumentPrepcessing |

#### How to run

```java
File folder = new File(path);//path为所在文件夹绝对路径
File[] fileSet = folder.listFiles();
DocumentPrepcessing dp;
dp = DocumentPrepcessing.getInstance(fileSet);
```

#### How to use

```java
documentPrepcessing = DocumentPrepcessing.getInstance();
```


## 测试模型生成模块

该部分的主要工作是根据对应服务所具有的操作类型来进行对应的模型选择.

所使用的类如下：

* TestPatternGeneration

### TestPatternGeneration

| 方法 | 说明 | 返回类型 |
| - | :- | :-: |
| run() | 运行函数 | 无 |
| getMode() | 获得所有的测试模型 | ConcurrentHashMap<String, ArrayList<String>> |

#### How to run

```java
TestPatternGeneration tpg = new TestPatternGeneration(dp.getOperaterTypesMap());
tpg.run();
```

#### How to use

```java
tpg.getMode();
```

## 测试模式及测试序列(脚本)生成模块

该部分根据前驱模块生成的测试模型来生成对应的操作序列，再根据操作序列来生成接口序列进而输出测试脚本.

所使用的类如下：

* ScriptGeneration
* SingleServiceSequenceGenerate
* GeneratingOperationSequence
* GeneratingInterfaceSequence

### How to run

```java
ScriptGeneration sg = new ScriptGeneration(path+"\\", tpg.getMode());
new Thread(sg).start();//开另一个线程运行提升效率
```

##  测试数据生成模块

该部分的主要工作是根据有条约束去生成所需要的数据.

**支持的数据类型**：

| 类型 | 说明 |
| :- | :- |
| string | 字符串 |
| token | 不包含空格(换字符、回车或制表符、开头或结尾空格或多个连续空格) |
| byte | 有正负的8位数 |
| decimal | 十进制数 |
| short | 有正负的16位数 |
| int | 有正负的32位数 |
| integer | 整数值 |
| long | 有正负的64位数 |
| unsignedLong | 无正负的64位数 |
| unsignedInt | 无正负的32位数 |
| unsignedShort | 无正负的16位数 |
| unsignedByte | 无正负的8位数 |
| negativeInteger | 仅包含负值的整数 ( .., -2, -1.) |
| nonNegativeInteger | 仅包含非负值的整数 (0, 1, 2, ..) |
| positiveInteger | 仅包含正值的整数 (1, 2, ..) |
| nonPositiveInteger | 仅包含非正值的整数 (.., -2, -1, 0) |
| float | 单精度浮点数 |
| double | 双精度浮点数 |
| boolean | 规定true或false值 |
| date | 定义一个日期值(格式为：YYYY-MM-DD) |
| dateTime | 定义一个日期和时间值(格式为：YYYY-MM-DDThh:mm:ss) |
| gDay | 定义日期的一个部分 - 天 (DD) |
| gMonth | 定义日期的一个部分 – 月 (MM) |
| gMonthDay | 定义日期的一个部分 – 月 和天(MM-DD) |
| gYear | 定义日期的一个部分 – 年 (YYYY) |
| gYearMonth | 定义日期的一个部分 – 年和月 (YYYY-MM) |
| time | 定义一个时间值(格式为：hh:mm:ss) |

**支持的约束类型**：

| 类型 | 说明 |
| :- | :- |
| enumeration | 枚举，把数据类型约束为指定的值 |
| totalDigits | 指定小数的最大位数 |
| fractionDigits | 指定小数点后的最大位数 |
| minExclusive | 最小值，不包含本身 |
| maxExclusive | 最大值，不包含本身 |
| minInclusive | 最小值，包含本身 |
| maxInclusive | 最大值，包含本身 |
| length | 固定长度 |
| minLength | 最小长度 |
| maxLength | 最大长度 |
| pattern | 正则表达式描述对应的匹配模式 |

**待支持的数据类型**：

| 类型 | 说明 |
| :- | :- |
| array | 数组类型 |
| set | 元素值不可重复 |
| list | 元素值可重复 |
| map | 元素为键值对 |
| duration |定义一个时间间隔(格式为：PnYnMnDTnHnMnS) |
| anyURI | 规定URI(统一资源标识符) |

**待支持的约束类型**：

| 类型 | 说明 |
| :- | :- |
| sensitive | 对字母大小写是否敏感，可选值：upper(仅含大写字母)、lower(仅含小写字母)、mix(大小写字母混合)|
| format | 文件类型，文件指图片(jpg、gif)、文档(txt、doc、xls) |
| minSize | 最小文件大小 |
| maxSize | 最大文件大小 |

所使用的工具类都在包`nwpu.autosystemtest.data`以及包`nwpu.autosystemtest.tools`中.

该部分的运行方法涉及的类如下:

* AutomatedTestData

### AutomatedTestData

该类提供了分析输入文件参数的数据类型及约束并调用对应的方法来进行数据文件的生成.

#### How to run 

```java
AutomatedTestData atd = new AutomatedTestData(path+"\\");
new Thread(atd).start();//开另一个线程运行提升效率
```

# 输出文件格式

## 文件结构

- 输入文件所在文件夹
	- log
	- testmodelfiles
	- operationsequencelfiles
	- outputxml
	- Data

### log

存放日志文件，目前日志文件仅记录输入文件解析，且由于并行导致无法按服务记录.

日志片段：
```txt
---------------------------Wed May 30 16:41:14 CST 2018---------------------------
service name:IAddTravelDateService
add InterfaceSet:
service name:ISellerPersonInChargeManageService
add InterfaceSet:
service name:IAddProductService
add InterfaceSet:
service name:IAdministerSellerManageService
add InterfaceSet:
  Operation [name=Add_TravelDate, id=addTravelDate, path=addTravelDate]
service name:IProductService
add InterfaceSet:
  Operation [name=Add_Product, id=addProduct, path=addProduct]
  Operation [name=Add_Seller, id=addSeller, path=addSeller]
  Operation [name=Add_Personincharge, id=addPersonincharge, path=addPersonincharge]
  Operation [name=Add_Product, id=addProduct, path=addProduct]
          addSeller-seller-idseller
          addSeller-seller-loginName[pattern:^[a-zA-Z0-9_]{3,16}$]
          addSeller-seller-passWord[pattern:^[a-zA-Z]\w{5,17}$]
          addSeller-seller-name[pattern:^[a-zA-Z0-9_]{3,16}$]
          addSeller-seller-mobilePhone[pattern:^1[34578][0-9]{9}$]
          addSeller-seller-telephone[pattern:^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$]
          addSeller-seller-email[pattern:^[a-zA-Z0-9_-]+\@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$]
          addSeller-seller-qq[pattern:^[1-9]\d{4,14}$]
          addSeller-seller-forbid
          addSeller-seller-company
          addSeller-seller-businessLicense
          addSeller-seller-productType
          addSeller-seller-pathType
          addSeller-seller-products
          addSeller-seller-personincharges
          addSeller-seller-receivermapnotices
          addSeller-seller-traffics
          addSeller-seller-materialinformations
          addSeller-seller-orders
    addSeller-seller
delete InterfaceSet:
  Operation [name=Delete_Seller, id=deleteSeller, path=deleteSeller]
    deleteSeller-seller
```
### testmodelfiles

存放所有服务的测试模型，文件名格式为`服务名+Model`

示例：

![](https://github.com/aalm1233/AutoSystem-Test/blob/master/resource/img/testmodels.png)

### operationsequencelfiles

存放所有服务的操作测试序列模式，文件名格式为`服务名+Sequence`

示例：

![](https://github.com/aalm1233/AutoSystem-Test/blob/master/resource/img/testsequence.png)

### outputxml

存放所有的测试脚本，文件目录结构如下:

+ outputxml
	+ 服务名
		+ 测试脚本长度
			+ 测试脚本

示例：

![](https://github.com/aalm1233/AutoSystem-Test/blob/master/resource/img/testscipt.png)

### Data

存放所有的测试数据,文件目录结构如下：

+ 服务名
	+ 操作类型
		+ 操作接口名
			+ 对应参数数据文件(xml格式) 

示例：

![](https://github.com/aalm1233/AutoSystem-Test/blob/master/resource/img/testdata.png)

## 测试脚本格式

<<<<<<< HEAD
测试脚本为了上下文一致以及后续的驱动解析方便采用xml格式.

测试脚本示例：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<script resourcesID="IAddTravelDateService" sequence="" completed="false">
	<step operation="add" path="addTravelDate" name="Add_TravelDate">
		<param name="travelDate" attribute="Traveldate" location="false">
			<element1 name="idtravelDate" attribute="Traveldate.idtravelDate" location="false">
			</element1>
			<element1 name="product" attribute="Product" location="false">
				<element2 name="idproduct" attribute="Product.idproduct" location="false">
				</element2>
				<element2 name="seller" attribute="Seller" location="false">
					<element3 name="idseller" attribute="Seller.idseller" location="false">
					</element3>
					<element3 name="loginName" attribute="Seller.loginName" location="false">
					</element3>
					<element3 name="passWord" attribute="Seller.passWord" location="false">
					</element3>
					<element3 name="name" attribute="Seller.name" location="false">
					</element3>
					<element3 name="mobilePhone" attribute="Seller.mobilePhone" location="false">
					</element3>
					<element3 name="telephone" attribute="Seller.telephone" location="false">
					</element3>
					<element3 name="email" attribute="Seller.email" location="false">
					</element3>
					<element3 name="qq" attribute="Seller.qq" location="false">
					</element3>
					<element3 name="forbid" attribute="Seller.forbid" location="false">
					</element3>
					<element3 name="company" attribute="Seller.company" location="false">
					</element3>
					<element3 name="businessLicense" attribute="Seller.businessLicense" location="false">
					</element3>
					<element3 name="productType" attribute="Seller.productType" location="false">
					</element3>
					<element3 name="pathType" attribute="Seller.pathType" location="false">
					</element3>
					<element3 name="products" attribute="Seller.products" location="false">
					</element3>
					<element3 name="personincharges" attribute="Seller.personincharges" location="false">
					</element3>
					<element3 name="receivermapnotices" attribute="Seller.receivermapnotices" location="false">
					</element3>
					<element3 name="traffics" attribute="Seller.traffics" location="false">
					</element3>
					<element3 name="materialinformations" attribute="Seller.materialinformations" location="false">
					</element3>
					<element3 name="orders" attribute="Seller.orders" location="false">
					</element3>
				</element2>
				<element2 name="type" attribute="Product.type" location="false">
				</element2>
				<element2 name="pathType" attribute="Product.pathType" location="false">
				</element2>
				<element2 name="warmPrompt" attribute="Product.warmPrompt" location="false">
				</element2>
				<element2 name="journeyWord" attribute="Product.journeyWord" location="false">
				</element2>
				<element2 name="title" attribute="Product.title" location="false">
				</element2>
				<element2 name="traveLagency" attribute="Product.traveLagency" location="false">
				</element2>
				<element2 name="feature" attribute="Product.feature" location="false">
				</element2>
				<element2 name="placeDeparture" attribute="Product.placeDeparture" location="false">
				</element2>
				<element2 name="destination" attribute="Product.destination" location="false">
				</element2>
				<element2 name="travelScheduleDays" attribute="Product.travelScheduleDays" location="false">
				</element2>
				<element2 name="travelScheduleNights" attribute="Product.travelScheduleNights" location="false">
				</element2>
				<element2 name="notesForApplication" attribute="Product.notesForApplication" location="false">
				</element2>
				<element2 name="priceInclude" attribute="Product.priceInclude" location="false">
				</element2>
				<element2 name="priceNotIncludes" attribute="Product.priceNotIncludes" location="false">
				</element2>
				<element2 name="salesInformation" attribute="Product.salesInformation" location="false">
				</element2>
				<element2 name="informationState" attribute="Product.informationState" location="false">
				</element2>
				<element2 name="salesState" attribute="Product.salesState" location="false">
				</element2>
				<element2 name="trafficInformation" attribute="Product.trafficInformation" location="false">
				</element2>
				<element2 name="themeLabele" attribute="Product.themeLabele" location="false">
				</element2>
				<element2 name="featureLabele" attribute="Product.featureLabele" location="false">
				</element2>
				<element2 name="publicIsNot" attribute="Product.publicIsNot" location="false">
				</element2>
				<element2 name="recommendLabele" attribute="Product.recommendLabele" location="false">
				</element2>
				<element2 name="updateTime" attribute="Product.updateTime" location="false">
				</element2>
				<element2 name="detailpicturedayses" attribute="Product.detailpicturedayses" location="false">
				</element2>
				<element2 name="detailpictures" attribute="Product.detailpictures" location="false">
				</element2>
				<element2 name="detailtexts" attribute="Product.detailtexts" location="false">
				</element2>
				<element2 name="orders" attribute="Product.orders" location="false">
				</element2>
				<element2 name="traveldates" attribute="Product.traveldates" location="false">
				</element2>
			</element1>
			<element1 name="travelDate" attribute="Traveldate.travelDate" location="false">
			</element1>
			<element1 name="price" attribute="Traveldate.price" location="false">
			</element1>
			<element1 name="priceTag" attribute="Traveldate.priceTag" location="false">
			</element1>
			<element1 name="remainingSeats" attribute="Traveldate.remainingSeats" location="false">
			</element1>
			<element1 name="childPrice" attribute="Traveldate.childPrice" location="false">
			</element1>
			<element1 name="childPriceTag" attribute="Traveldate.childPriceTag" location="false">
			</element1>
			<element1 name="childRemainingSeats" attribute="Traveldate.childRemainingSeats" location="false">
			</element1>
			<element1 name="orders" attribute="Traveldate.orders" location="false">
			</element1>
		</param>
		<response type="">
		</response>
	</step>
	<step operation="find" path="findTravelDateById" name="Find_TravelDate_ById">
		<param name="id" attribute="Traveldate.idtravelDate" location="false">
		</param>
		<response type="Traveldate">
			<param name="travelDate" attribute="Traveldate">
			</param>
		</response>
	</step>
</script>

```

**标签定义如下**：

| 标签名 | 描述内容 | 属性 |
| :-: |:-|:-|
| script | 脚本开始/结束 | resourcesId  服务标识 |
| step | 执行步骤 | operation  接口类型<br>path  接口访问路径<br>resourceId 接口标识 |
| param | 接口参数 |name  参数名称<br>attribute  对应实体属性名称<br>location 对象定位 |
| element | 容器类型元素 | name  元素名称<br>attribute  对应实体属性名称<br>location 对象定位 |
| response | 响应数据 | dataType 返回数据类型 |

### 测试数据格式

测试数据为了上下文一致以及后续的驱动解析方便采用xml格式.

测试数据示例：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<param name="personincharge" attribute="Personincharge" status="generate">
<element1 name="idpersonIncharge" attribute="Personincharge.idpersonIncharge" status="generate" >
	<value>652</value>
	<value>1</value>
	<value>5</value>
	<value>6</value>
	<value>3</value>
	<value>-2147483647</value>
	<value>-2147483648</value>
	<value>2147483646</value>
	<value>2147483647</value>
	<value>0</value>
</element1>
<element1 name="seller" attribute="Seller" status="Seller" >
</element1>
<element1 name="name" attribute="Personincharge.name" status="generate" >
	<value>V1___zi0_8_</value>
	<value>3__</value>
	<value>_95_W8VB__</value>
	<value>d2_</value>
	<value>QL</value>
</element1>
<element1 name="mobilPhone" attribute="Personincharge.mobilPhone" status="generate" >
	<value>18387512968</value>
	<value>14063735156</value>
	<value>17355473481</value>
	<value>17244010118</value>
	<value>13858784952</value>
</element1>
<element1 name="telephone" attribute="Personincharge.telephone" status="generate" >
	<value>34958653-4273</value>
	<value>7463474</value>
	<value>536462004144138-31</value>
	<value>0738-86185550</value>
	<value>7679844-5</value>
</element1>
<element1 name="email" attribute="Personincharge.email" status="generate" >
	<value>8f--B__-K1@U7.-</value>
	<value>T0@FL_.-</value>
	<value>n1t-___6@5.9-_6W.__3-a.90-3-0-2_G_----2--</value>
	<value>y_OP__330_I-H@70P.D-4F_</value>
	<value>X-N_-q5mIe-@UP08B-___X._SC_</value>
</element1>
<element1 name="qq" attribute="Personincharge.qq" status="generate" >
	<value>84865561301</value>
	<value>4623518</value>
	<value>64931</value>
	<value>6538533</value>
	<value>281773215</value>
</element1>
</param>
```
=======
测试脚本为了上下文一致以及后续的驱动解析方便
>>>>>>> eca0373f19894ba5126b1a4dd96625bb66a61d40
