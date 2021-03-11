<#import "parts/common.ftl" as c>


<@c.page>

<br>
<script src="/js/scripts.js"></script>
<script language="JavaScript">

    function toggleCheckbox(elem) {

        if(elem.checked) {

        }
        else
        {

        }
    }


</script>

<br>
<div>
    <#if error?has_content>
    ${error}
</#if>
</div>
<br>


<div class="form-row">
    <div class="form-group col-md-10">
        <form method="post" enctype="multipart/form-data" id="js-upload-form">

            <div><h3 class="display-4">Контактное лицо</h3></div>
            <br>
            <div class="jumbotron">
                <h4 class="display-5">Информация</h4>

                <div class="form-group row mt-3">
                    <div class="col-sm-9">
                        <#if contact.fio??>
                        <input type="text" class="form-control" name="fio" placeholder="Ф.И.О." value="${contact.fio}"/>
                        <#else>
                        <input type="text" class="form-control" name="fio" placeholder="Ф.И.О."/>

                    </#if>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if contact.position??>
                    <input type="text" class="form-control" name="position" placeholder="Должность" value="${contact.position}"/>
                    <#else>
                    <input type="text" class="form-control" name="position" placeholder="Должность"/>

                </#if>
            </div>
            </div>
            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if contact.phoneNumber??>
                    <input type="text" class="form-control" name="phoneNumber" placeholder="Номер телефона" value="${contact.phoneNumber}"/>
                    <#else>
                    <input type="text" class="form-control" name="phoneNumber" placeholder="Номер телефона"/>

                </#if>
            </div>
            </div>
            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if contact.email??>
                    <input type="text" class="form-control" name="email" placeholder="E-mail" value="${contact.email}"/>
                    <#else>
                    <input type="text" class="form-control" name="email" placeholder="E-mail"/>

                </#if>
            </div>
            </div>




</div>

<button type="submit" id="submit" class="btn btn-primary mb-2">Изменить данные контакта</button>
<button class="btn btn-secondary mb-2" onclick="window.close();window.open('${urlprefixPath}/showone${sourcetype}?id=${sourceid}')">Выйти без изменения</button>




<input type="hidden" name="_csrf" value="${_csrf.token}">

</form>
</div>
</div>

<br>


</@c.page>