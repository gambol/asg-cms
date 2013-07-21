<!--<!DOCTYPE html>-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<%@page  language="java" contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <%@include file="../common/appInclude.jsp" %>    
    <body>
        <script type="text/javascript">
            $(document).ready(function(){
                var submitted = false;
                // validate form on keyup and submit
                var validator = $("#regiForm").validate({
                    rules: {
                        userEmail: {
                            required: true,
                            email: true,
                            remote: "/user/check.htm"
                        },
                        password:{
                            required:true
                         
                        },
                        repassword:{
                            required:true,
                            equalTo:"#password"
                        },
                        captcha: {
                            required: true,
                            remote:"/validateImage.htm"
                        },
                        tos:{
                            required:true
                        },
                        terms: "required"
                    },
                    messages: {
                        userEmail: {
                            required: "请输入用户名",
                            email:"用户名必须是有效邮箱",
                            remote: "用户名已存在"
                        },
                        password:{
                            required:"密码不能为空" 
                        },
                        repassword:{
                            required:"密码不能为空",
                            equalTo:"两次输入必须一致"
                        },
                        captcha: {
                            required:"请输入验证码",
                            remote:"验证码错误"
                        },
                        tos:{
                            required:"要使用我们的服务，您必须同意接受的用户协议。"
                        },
                        terms: ""
                    },
                    showErrors: function(errorMap, errorList) {  
                        $.each(errorMap, function(name, value) {
                            if(name != "tos"){
                                $("#" + name + "ErrLb").hide();
                                $("#" + name + "ErrImg").show();
                            }
                            
                            //                            $("#" + name + "ErrLb").show();
                            //                            $("#" + name + "ErrLb").html(value);                       
                        });
                        this.defaultShowErrors();
                    },

                    
                    // the errorPlacement has to take the layout into account
                    errorPlacement: function(error, element) {
                        error.insertAfter($("#"+element.attr("name")+"ErrImg"));
                    },
                    
                    // specifying a submitHandler prevents the default submit, good for the demo
                    /*
                    submitHandler: function() {
                        alert("Data submitted!");
                    },
                     */
                    // set new class to error-labels to indicate valid fields
                    success: function(label) {
                        $("#" + label.attr("for") + "ErrImg").hide();
                    }
                });
            });
            
            function refreshImage(){    
        
                $('#captchaImg').hide().attr('src',"/generateImage.htm?" + Math.floor(Math.random()*100)).fadeIn();     
            }    
        </script>
        <style>


            label.error {
                vertical-align: middle;                        
            }</style>
        <!-- Header -->
        <%@include file="../common/header.jsp" %>
        <!-- End of Header -->
        <!-- Page title -->
        <!--
            <div id="pagetitle">
                    <div class="wrapper-login"></div>
            </div>
        <!-- End of Page title -->

        <!-- Page content -->
        <div id="page">
            <!-- Wrapper -->
            <div class="wrapper">

                <!-- Login form -->

                <section class="width5">					
                    <div class="pageline">   
                    </div>

                    <div class="content-box">
                        <header class="blue">
                            <h3>注册</h3>
                            <div class="right-href" ><a href="/user/login.htm">已有账号直接登陆</a></div>
                        </header>

                    </div>
                    <form:form id="regiForm" method="post" modelAttribute="userBean" cssClass="cleanform">

                        <p>
                            <form:label cssClass="required" path="userEmail">邮箱：</form:label><br/>
                            <!--                            <div style="width: 280px;float: right">-->
                            <form:input type="text" path="userEmail" cssClass="half " value=""/>


                            <c:choose>
                                <c:when test="${not empty mailErrMsg}">
                                    <img id="userEmailErrImg" src="/img/alert.png" style="vertical-align: middle"/>
                                    <label id="userEmailErrLb" style="vertical-align: middle;color:#DD0000">${mailErrMsg}</label>
                                </c:when>
                                <c:when test="${empty mailErrMsg}">
                                    <img id="userEmailErrImg" src="/img/alert.png" style="vertical-align: middle;display: none" />
                                    <label id="userEmailErrLb" style="vertical-align: middle;display: none;color:#DD0000">${mailErrMsg}</label>
                                </c:when>
                            </c:choose>


                            <!--                            %>-->

                        </p>


                        <p>
                            <form:label cssClass="required" path="password">密码:</form:label><br/>
                            <form:input type="password" path="password" cssClass="half" value=""/>    
                            <img id="passwordErrImg" src="/img/alert.png" style="vertical-align: middle;display: none" />
                            <label id="passwordErrLb" style="vertical-align: middle;display: none">${errMsg}</label>
                        </p>

                        <p>
                            <form:label cssClass="required" path="repassword">再输入一遍：</form:label><br/>
                            <form:input type="password" path="repassword" cssClass="half" value=""/>    
                            <img id="repasswordErrImg" src="/img/alert.png" style="vertical-align: middle;display: none" />
                            <label id="repasswordErrLb" style="vertical-align: middle;display: none">${errMsg}</label>
                        </p>

                        <p> 
                            <label class="required" for="captcha">验证码:</label><br/>

                            <input type="text" name="captcha" id="captcha"  style="vertical-align: middle;width: 27%"/>                            
                            <img id='captchaImg' alt="验证码" src="/generateImage.htm" style="height: 25px;vertical-align: middle"/> 
                            <span  style="vertical-align: bottom"><a href="javascript:void(0)" onclick="refreshImage()">看不清?换一张</a></span>
                            <img id="captchaErrImg" src="/img/alert.png" style="vertical-align: middle;display: none" />


                        </p>
                        <p>

                            <label>
                                <input id="tos" type="checkbox" name="tos" value="yes">
                                <span id="terms-of-service-label">
                                    <strong>
                                        我已阅读并同意接受  Atugame
                                        <a id="TosLink" href="/tos.htm" target="_blank">用户协议</a>                                        。
                                    </strong>
                                </span>
                            </label>
                            <br/>
                            <img id="tosErrImg" src="/img/alert.png" style="vertical-align: middle;display: none"/>

                            <c:if test="${not empty tosErrMsg}">                                    
                                <label id="tosErrLb" style="vertical-align: middle;color:#DD0000">${tosErrMsg}</label>
                            </c:if>


                        </p>


                        <div >
                            <input type="submit" class="btn btn-yellow big" value="    注   册   "/>

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

        <!-- Page footer -->
        <%@include file="../common/footer.jsp" %>
        <!-- End of Animated footer -->

        <!-- Scroll to top link -->
        <a href="#" id="totop">^ scroll to top</a>

        <!-- User interface javascript load -->
    </body>
</html>
