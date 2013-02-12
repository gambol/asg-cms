<%-- 
    Document   : nain
    Created on : Jul 16, 2012, 7:08:42 PM
    Author     : xiang.fu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html class="js" lang="en"><!--<![endif]-->
    <head>
        <jsp:include page="head.jsp"/>
    </head>

    <body id="homepage" class="">                
        <jsp:include page="header.jsp"/>
        <input id="curStart" type="hidden" value="0"/>
        <input id="size" type="hidden" value="50"/>
        <input id="totalCount" type="hidden" value="0"/>

        <nav role="navigation" class="clearfix">
            <ul class="main-nav">
                <li>
                    <a href="#" onclick="changeCategory('1');return false;">传奇私服</a>
                </li>
                <li>
                    <a href="#" onclick="changeCategory('2');return false;">魔兽世界</a>

                </li>
            </ul>
        </nav>
        <div id="content">
            <div class="container">
                <div id="rank-title" class="shadowed guest">
                    <div id="rank-title-header"></div>
                    <div id="rank-title-name">魔兽世界私服排名</div>

                    <div class ="rank-title-sep"></div>
                    <div id="rank-title-page" >
                        <ul>
                            <li>
                                <div class="rank-page">1-50 名</div> 
                                <div class="rank-page-dropdown">                                               

                                </div>
                            </li>
                        </ul>

                    </div>            
                </div>



                <div id="right-side-bar" class=" shadowed">
                    <div class="inner-boundary">
                        <div class="inner-border">

                            <div class="thumbnail itemcontainer">
                                <a href="#" onclick="">
                                    <img src="images/80x80game.jpg" alt="GAME STYLES V2 - GraphicRiver Item for Sale" title="GAME STYLES V2" class="square-image-magnifier preload no_preview" data-preview-width="" data-preview-height="" data-item-name="GAME STYLES V2" data-item-author="INDUSTRYKIDZ" data-item-category="Add-ons / Photoshop / Styles" data-item-cost="4" data-preview-url="http://0.s3.envato.com/files/3422603/GAMEPREVIEW2.jpg" border="0" height="80" width="80">
                                </a>            </div>
                            <a href="#"><h3 class="decorator">Free File!</h3></a>
                            <p>
                                Grab this month's <a href="#">free file</a> from the Text Effects category!
                            </p>

                        </div>
                    </div>
                </div>
                <div id="waitting-bar" class="shadowed">
                    <div class="inner-boundary">
                        <div class="inner-border">
                            <img src="images/waiting.gif" />
                        </div>
                    </div>
                </div>

                <div id="rank-content-all">                  
                </div>
            </div>
            <div class="clear">  </div>
        </div>
    </div>
    <script>changeCategory('1');</script>
    <jsp:include page="footer.jsp"/>
</body>
</html>
