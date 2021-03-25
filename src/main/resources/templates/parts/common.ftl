<#macro page>
<#include "security.ftl">
<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>CLB</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">


    <!-- Bootstrap CSS -->

                    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
                    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <!--
                        <link href="http://gren-wd-000578:8021/CLB/js/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
-->

        <link rel="shortcut icon" href="http://${hostname}${urlprefixPath}/img/favicon.ico" type="image/x-icon">

    <!--
    <script src="/js/scripts.js"></script>
    -->

</head>
<body>
    <#include "navbar.ftl">
<div class="container mt-3">
    <#nested>
</div>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->

    <!--
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>



    <script src="http://gren-wd-000578:8021/CLB/js/jquery-3.3.1.slim.min.js" crossorigin="anonymous"></script>
    <script src="http://gren-wd-000578:8021/CLB/js/popper.min.js" crossorigin="anonymous"></script>
    <script src="http://gren-wd-000578:8021/CLB/js/bootstrap.min.js" crossorigin="anonymous"></script>
    -->


</body>
</html>

</#macro>