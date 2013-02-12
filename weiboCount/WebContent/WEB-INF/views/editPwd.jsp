<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ page session="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <%@include file="../common/appInclude.jsp" %>
    <body>
        <%@include file="../common/header.jsp" %>
        <script>
            selectMenu("menu-userCenter");
        </script>
        
        <script type="text/javascript" src="/js/sitemanage.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                var submitted = false;
                // validate form on keyup and submit
                var validator = $("#editPwdForm").validate({
                    rules: {
                        oldPassword: {
                            required: true                          
                        },
                        password:{
                            required:true
                         
                        },
                        repassword:{
                            required:true,
                            equalTo:"#password"
                        },
                       
                        terms: "required"
                    },
                    messages: {
                         oldPassword: {
                            required: "旧密码不能为空"                          
                        },
                        password:{
                            required:"密码不能为空" 
                        },
                        repassword:{
                            required:"密码不能为空",
                            equalTo:"两次输入必须一致"
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
           
        </script>
        <style>


            label.error {
                vertical-align: middle;                        
            }</style>
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
                <section class="column width1 first">

                    <div class="site-nav">
                        <ul>
                            <li ><a href="/user/sitemanage.htm">发布记录</a></li>
                            <li><a href="/user/publish.htm">新站发布</a></li>
                            <li class="current"><a href="/user/editPassword.htm">修改密码</a></li>
                            <!--<li><a href="#">修改密码</a></li> -->

                        </ul>
                    </div>  
                </section>
                <section class="width5">					
                    <div class="pageline">   
                    </div>

                    <div class="content-box">
                        <header class="blue">
                            <h3>修改密码</h3>
                            
                        </header>

                    </div>
                    <form:form id="editPwdForm" method="post" modelAttribute="userBean" cssClass="cleanform">

                        
                        <p>
                            <form:label cssClass="required" path="oldPassword">旧密码:</form:label><br/>
                            <form:input type="password" path="oldPassword" cssClass="half" value=""/>    
                            <c:choose>
                                <c:when test="${not empty oldPwdErrMsg}">
                                    <img id="oldPasswordErrImg" src="/img/alert.png" style="vertical-align: middle" />
                                    <label id="oldPasswordErrLb" style="vertical-align: middle">${oldPwdErrMsg}</label>
                                </c:when>
                                <c:when test="${empty oldPwdErrMsg}">
                                    <img id="oldPasswordErrImg" src="/img/alert.png" style="vertical-align: middle;display: none" />
                                    <label id="oldPasswordErrLb" style="vertical-align: middle;display: none">${oldPwdErrMsg}</label>
                                </c:when>
                            </c:choose>
                        </p>

                        <p>
                            <form:label cssClass="required" path="password">新密码:</form:label><br/>
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

                        


                        <div >
                            <input type="submit" class="btn btn-yellow big" value="    修   改   "/>

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

