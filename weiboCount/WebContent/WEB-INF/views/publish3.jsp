<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <%@ include file="/WEB-INF/common/header.jsp" %>
        <link rel="stylesheet" type="text/css" media="screen" href="styles/css/publish.css">
    <script type="text/javascript" src="styles/js/jquery.validate.js"></script>
    <script type="text/javascript" src="styles/js/jquery.validate.cn.js" charset="UTF-8" ></script>
   
    <script type="text/javascript">
        $(document).ready(function(){
            // validate form on keyup and submit
            var validator = $("#publishform").validate({
                rules: {
                    firstname: "required",
                    lastname: "required",
                    username: {
                        required: true,
                        minlength: 2
                    },
                    password: {
                        required: true,
                        minlength: 5
                    },
                    password_confirm: {
                        required: true,
                        minlength: 5,
                        equalTo: "#password"
                    },
                    email: {
                        required: true,
                        email: true
                    },
                    dateformat: "required",
                    terms: "required"
                },
                messages: {
                    firstname: "Enter your firstname",
                    lastname: "Enter your lastname",
                    username: {
                        required: "Enter a username",
                        minlength: jQuery.format("Enter at least {0} characters")
                    },
                    password: {
                        required: "Provide a password",
                        rangelength: jQuery.format("Enter at least {0} characters")
                    },
                    password_confirm: {
                        required: "Repeat your password",
                        minlength: jQuery.format("Enter at least {0} characters"),
                        equalTo: "Enter the same password as above"
                    },
                    email: {
                        required: "Please enter a valid email address",
                        minlength: "Please enter a valid email address"
                    },
                    dateformat: "Choose your preferred dateformat",
                    terms: " "
                },
                // the errorPlacement has to take the layout into account
                errorPlacement: function(error, element) {
                    error.insertAfter(element.parent().find('label:first'));
                },
                // specifying a submitHandler prevents the default submit, good for the demo
                submitHandler: function() {
                    alert("Data submitted!");
                },
                // set new class to error-labels to indicate valid fields
                success: function(label) {
                    // set &nbsp; as text for IE
                    label.html("&nbsp;").addClass("ok");
                }
            });

        });
    </script>
    <body>
        <div class="container">

            <div id="formsContent">
                <h2>私服发布</h2>
                <p class="write">如果您选择在这里发布私服，您已经默认遵守以下条约</p>
                <p class="write">Atugame里不允许有任何欺骗。欺骗的行为包括以下:</p>
                <p class="write">Atugame的管理员有权利移除、修改、编辑任何一个私服网站的信息。我们保证不会将您的信息出卖给任何一个其他网站</p>
                <p class="write">如果您觉得某一个私服站长进行了欺骗，请联系我们</p>	

                <form:form id="publishform" method="post" modelAttribute="publishBean" cssClass="cleanform">
                    <div class="header">
                        <c:if test="${not empty message}">
                            <div id="message" class="success">${message}</div>	
                        </c:if>
                        <s:bind path="*">
                            <c:if test="${status.error}">
                                <div id="message" class="publishError">Form has errors</div>
                            </c:if>
                        </s:bind>"
                    </div>

                    <form:label path="name">
                        用户名:
                    </form:label>
                    <form:input path="name" cssClass="required" minlength="2"/>

                    <form:label path="passwd">
                        密码:
                    </form:label>
                    <form:password path="passwd" cssClass="required" minlength="6"/>

                    <form:label path="email">
                        邮箱: 
                    </form:label>
                    <form:input path="email" cssClass="required email"/>

                    <form:label path="title">
                        网站名: 
                    </form:label>
                    <form:input path="title" cssClass="required" />

                    <form:label path="siteUrl">
                        URL:
                    </form:label>
                    <form:input path="siteUrl" cssClass="url required"/>

                    <form:label path="bannerUrl">
                        网站图片:
                    </form:label>
                    <form:input path="bannerUrl" cssClass="url required"/>

                    <form:label path="description">
                        网站描述: 
                    </form:label>
                    <form:textarea path="description" cssClass="required" maxlength="1000" />

                    <form:label path="category">
                        分类:
                    </form:label>
                    <form:select path="category">
                        <form:option value="1">传奇</form:option>
                        <form:option value="2">魔兽世界</form:option>
                    </form:select>

                    <p><button type="submit">Submit</button></p>
                </form:form>

            </div>
            <script type="text/javascript">
                /*
                    $(document).ready(function() {
                            $("#form").submit(function() {  
                                    $.post($(this).attr("action"), $(this).serialize(), function(html) {
                                            $("#formsContent").replaceWith(html);
                                            $('html, body').animate({ scrollTop: $("#message").offset().top }, 500);
                                    });
                                    return false;  
                            });			
                    });
                 */
            </script>
        </div>
    </div>
    
    <%@ include file="/WEB-INF/common/footer.jsp" %>   

</body>
</html>
