
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ page session="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <%@include file="../common/appInclude.jsp" %>
    <link rel="stylesheet" href="/styles/css/publish.css" type="text/css" />
    <body>
        <%@include file="../common/header.jsp" %>
        <script>
            selectMenu("menu-userCenter");
        </script>
        <!-- End of Header -->
        <!-- Page title -->
        <!--
            <div id="pagetitle">
                    <div class="wrapper-login"></div>
            </div>
        <!-- End of Page title -->
        <script type="text/javascript">
            $(document).ready(function(){
                // validate form on keyup and submit
                var validator = $("#publishform").validate({
                    rules: {
                        server_name: {
                            required: true,
                            minlength: 2,
                            maxlength: 40
                        },
                        url: {
                            required: true,
                            minlength: 5,
                            url: true
                        },
                        desc: {
                            required: true,
                            minlength: 5,
                            maxlength: 1500
                        },
                        captcha: {
                            required: true,
                            remote:"/validateImage.htm"
                        }
                       
                    },
                    messages: {
                        server_name: {
                            required: "输入站点名称",
                            minlength: jQuery.format("至少需要{0} 个字符")
                        },
                        url: {
                            required: "输入站点url",
                            minlength: jQuery.format("至少需要 {0} 个字符"),
                            url: "请输入合法的网址"
                        },
                        desc: {
                            required: "输入描述",
                            minlength: jQuery.format("至少需要{0} 个字符"),
                            maxlength: jQuery.format("长度不超过{0}个字符")
                        },
                        captcha: {
                            required: "输入验证码",
                            remote: "请输入正确的验证码"
                        }
                      
                    },
                    // the errorPlacement has to take the layout into account
                    errorPlacement: function(error, element) {                      
                        //                             error.insertAfter(element.parent().find('input:first'));
                   
                        error.appendTo (element.next());
                       
                    },
                    // specifying a submitHandler prevents the default submit, good for the demo
                    /*
                            submitHandler: function() {
                                alert("Data submitted!");
                            },
                     */
                    // set new class to error-labels to indicate valid fields
                    success: function(label) {
                        // set &nbsp; as text for IE
                      
                             label.html("&nbsp;").addClass("checked");
                        /*
                        var a = "label.error[for='"+label.attr("for")+"']";
                        alert(a);
                        $("label.error[for='"+label.attr("for")+"']").classname('');
                        $("label.error[for='server_name']").classname('');
                        */
                    }
                });
            });
            
            function refreshImage(){    
        
                $('#captchaImg').hide().attr('src',"/generateImage.htm?" + Math.floor(Math.random()*100)).fadeIn();     
            }    
        </script>
        <!-- Page content -->
        <div id="page">
            <!-- Wrapper -->
            <div class="wrapper">
                <section class="column width1 first">
                    <div class="site-nav">
                        <ul>
                            <li  <c:if test="${not empty publishBean['server_name']}"> class="current" </c:if>><a href="/user/sitemanage.htm">发布记录</a></li>
                            <li  <c:if test="${empty publishBean['server_name']}"> class="current" </c:if>><a href="/user/publish.htm">新站发布</a></li>
                            <li><a href="/user/editPassword.htm">修改密码</a></li>
                               <!-- <li><a href="#">修改密码</a></li> -->

                            </ul>
                        </div>  
                    </section>


                    <section class="width5">					
                        <div class="pagetitle">
                            <header class="blue">
                                <h3>站点发布</h3>
                            </header>

                        </div>

                    <form:form id="publishform" method="post" modelAttribute="publishBean" cssClass="cleanform">
                        <p>
                            <c:if test="${not empty message}">
                            <div id="message" class="success">${message}</div>	
                            </c:if>
                            <s:bind path="*">
                                <c:if test="${status.error}">
                                    <div id="message" class="box-error">请正确输入</div>
                                </c:if>
                            </s:bind>
                        </p>
                        <p>
                            <form:label cssClass="required" path="server_name">站点名称:</form:label><br/>
                            <form:input type="text" path="server_name" cssClass="half" value=""/>
                            <span/>
                        </p>

                        <p>
                            <form:label cssClass="required" path="url">链接:</form:label><br/>
                            <form:input type="text" path="url" cssClass="half" value=""/>  
                            <span/>
                        </p>


                        <p>
                            <form:label  path="banner">图片:</form:label><br/>
                            <form:input type="file" path="banner" cssClass="half" value=""/>   
                            <span/>
                        </p>

                        <p>
                            <form:label cssClass="required" path="network">网络:</form:label><br/>
                            <form:radiobutton path="network" value="网通" label="网通 "/>
                            <form:radiobutton path="network" value="电信" label="电信 "/>
                            <form:radiobutton path="network" value="双线" label="双线 "/>
                            <form:radiobutton path="network" value="铁通" label="铁通 "/>
                            <form:radiobutton path="network" value="联通" label="联通 "/>
                            <span/>
                        </p>


                        <p>
                            <form:label cssClass="required" path="category">游戏种类:</form:label>
                                <br>
                            <form:select path="category" cssClass="half">
                                <form:option value="1" label="传奇"/>
                                <form:option value="2" label="魔兽世界"/>
                            </form:select>
                            <span/>
                        </p>
                        <p>
                            <form:label cssClass="required" path="desc">描述:</form:label><br/>
                            <form:textarea path="desc" class="large full"/>
                            <span/>
                        </p>

                        <p> <label class="required" for="captcha">验证码:</label> <br/>
                            <img id='captchaImg' alt="验证码" src="/generateImage.htm"> <a href="javascript:void(0)" onclick="refreshImage()">看不清?换一张</a>  <br/>                       
                            <input type="text" name="captcha" id="captcha" class="half"/>
                            <span/>
                        </p>
                        <br/>
                        <!--
                        <c:if test="${empty publishBean['server_name']}">
                            <div class="contract">
                                <p>
								　　新浪体育讯　北京时间8月12日，中国代表团伦敦奥运会总结大会在奥运主新闻中心新闻发布厅举行，中国代表团团长刘鹏、副团长段世杰、杨树安、肖天、蔡振华，秘书长蔡家东，以及主持人张海峰出席了本次中国奥运代表团发布会。
                                </p>
                                <p>
							　　	此次伦敦奥运会上中国军团的运动员们取得了非常出色的成绩，年轻的游泳运动员叶诗文(微博)43万更是取得了用两枚金牌，而赛后曾有很多外国媒体却一直纠缠于兴奋剂的问题上，对于这个事件现场有记者对中国代表团团长刘鹏进行了提问，他是如何看待国外媒体质疑总过游泳运动员兴奋剂问题的。
                                </p>
                                <p>
								　　新浪体育讯　北京时间8月12日，中国代表团伦敦奥运会总结大会在奥运主新闻中心新闻发布厅举行，中国代表团团长刘鹏、副团长段世杰、杨树安、肖天、蔡振华，秘书长蔡家东，以及主持人张海峰出席了本次中国奥运代表团发布会。
                                </p>
                                <p>
							　　	此次伦敦奥运会上中国军团的运动员们取得了非常出色的成绩，年轻的游泳运动员叶诗文(微博)43万更是取得了用两枚金牌，而赛后曾有很多外国媒体却一直纠缠于兴奋剂的问题上，对于这个事件现场有记者对中国代表团团长刘鹏进行了提问，他是如何看待国外媒体质疑总过游泳运动员兴奋剂问题的。
                                </p>
                            </div>
                            <input type="checkbox" id="terms" class="" value="1" name="terms" checked/>
                            <label class="choice" for="terms">我已阅读并接受以上合同条款、补充条款及其他所有内容</label>
                            <span/>
                        </c:if>
                        -->
                        <br/>
                        <br/>
                        <form:hidden path="id"/>
                        <div class="middle-div">
                            <input type="submit" class="btn btn-yellow big" value="    提    交    "/>
                        </div>

                        <br/>
                        <br/>

                    </form:form>

                </section>
                <!-- End of login form -->

            </div>
            <!-- End of Wrapper -->
        </div>
        <!-- End of Page content -->

        <%@include file="../common/footer.jsp" %>
    </body>
</html>
