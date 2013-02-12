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
                var validator = $("#loginForm").validate({
                    rules: {
                        userEmail: {
                            required: true,
                            email: true
                            //                            remote: "/user/check.htm"
                        },
                        password:{
                            required:true
                         
                        },
                        captcha: {
                            required: true,
                            remote:"/validateImage.htm"
                        },
                        terms: "required"
                    },
                    messages: {
                        userEmail: {
                            required: "请输入用户名",
                            email:"用户名必须是有效邮箱"
                            //                            remote: "用户名已存在"
                        },
                        password:{
                            required:"密码不能为空" 
                        },
                        captcha: {
                            required:"请输入验证码",
                            remote:"验证码错误"
                        },
                        terms: ""
                    },
                    showErrors: function(errorMap, errorList) {  
                        $.each(errorMap, function(name, value) {
                            $("#" + name + "ErrLb").hide();
                            $("#" + name + "ErrImg").show();
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
                            <h3>登录</h3>
                            <div class="right-href" ><a href="/user/regi.htm">没有账号，去注册一个</a></div>
                        </header>

                    </div>

                    <form:form id="loginForm" method="post" modelAttribute="userBean" cssClass="cleanform">

                        <p>
                            <form:label cssClass="required" path="userEmail">邮箱：</form:label><br/>
                                <!--                            <div style="width: 280px;float: right">-->
                            <form:input type="text" path="userEmail" cssClass="half " value=""/>


                            <c:choose>
                                <c:when test="${not empty mailErrMsg}">
                                    <img id="userEmailErrImg" src="/img/alert.png" class="error-img"/>
                                    <label id="userEmailErrLb" class="error-lb">${mailErrMsg}</label>
                                </c:when>
                                <c:when test="${empty mailErrMsg}">
                                    <img id="userEmailErrImg" src="/img/alert.png" class="error-img" style="display: none" />
                                    <label id="userEmailErrLb" class="error-lb" style="display: none;">${mailErrMsg}</label>
                                </c:when>
                            </c:choose>


                            <!--                            %>-->

                        </p>


                        <p>
                            <form:label cssClass="required" path="password">密码:</form:label><br/>
                            <form:input type="password" path="password" cssClass="half" value=""/>    
                            <img id="passwordErrImg" src="/img/alert.png" class="error-img" style="display: none" />
                            <label id="passwordErrLb" class="error-lb" style="display: none">${errMsg}</label>
                        </p>



                        <p> 
                            <label class="required" for="captcha">验证码:</label><br/>

                            <input type="text" name="captcha" id="captcha"  style="vertical-align: middle;width: 27%"/>                            
                            <img id='captchaImg' alt="验证码" src="/generateImage.htm" style="height: 25px;vertical-align: middle"/> 
                            <span  style="vertical-align: bottom"><a href="javascript:void(0)" onclick="refreshImage()">看不清?换一张</a></span>
                            <img id="captchaErrImg" src="/img/alert.png" style="vertical-align: middle;display: none" />


                        </p>
                        <br/>


                        <div >
                            <input type="submit" class="btn btn-yellow big" value="    登   录   "/>
                            <a href="/user/forget.htm" style="vertical-align: bottom">找回密码</a>
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
