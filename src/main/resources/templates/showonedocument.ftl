<#import "parts/common.ftl" as c>


<@c.page>

<br>
    <script src="/js/scripts.js"></script>
    <script language="JavaScript">

        function toggleCheckbox(elem) {

            if(elem.checked) {
                document.getElementById('validuntildatediv').hidden = true;
            }
            else
            {
                document.getElementById('validuntildatediv').hidden = false;
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

            <div><h3 class="display-4">Документ № <#if document?has_content>${document.documentNumber}</#if></h3></div>
            <br>
            <div class="jumbotron">
                <h4 class="display-5">Реквизиты</h4>

                <div class="form-group row mt-3">
                    <div class="col-sm-9">
                        <#if document.documentNumber??>
                            <input type="text" class="form-control" name="documentnumber" placeholder="Номер документа" value="${document.documentNumber}"/>
                        <#else>
                            <input type="text" class="form-control" name="documentnumber" placeholder="Номер документа"/>

                        </#if>
                    </div>
                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-9">
                        <#if document.documentName??>
                            <input type="text" class="form-control" name="documentname" placeholder="Номер документа" value="${document.documentName}"/>
                        <#else>
                            <input type="text" class="form-control" name="documentname" placeholder="Номер документа"/>

                        </#if>
                    </div>
                </div>


                <div class="form-group row mt-3">
                    <div class="col-auto">
                        <label for="inputDate">Дата документа</label>
                        <#if dbdocumentdate??>
                            <input type="date" class="form-control" name="documentdate" placeholder="Дата документа" value="${dbdocumentdate}"/>
                        <#else>
                            <input type="date" class="form-control" name="documentdate" placeholder="Дата документа"/>
                        </#if>
                    </div>
                </div>


                <div id="validuntildatediv" class="form-group row mt-3" <#if dbvaliduntildate == "3020-01-01">hidden</#if>>
                    <div class="col-auto">
                        <label for="inputDate">Срок действия</label>
                        <#if dbvaliduntildate??>
                            <input type="date" class="form-control" id="validuntildate" name="validuntildate" placeholder="Срок действия" value="<#if dbvaliduntildate != "3020-01-01">${dbvaliduntildate}</#if>"/>
                        <#else>
                            <input type="date" class="form-control" id="validuntildate" name="validuntildate" placeholder="Срок действия"/>
                        </#if>
                    </div>
                </div>


                <div class="custom-control custom-checkbox">
                    <input type="checkbox" class="custom-control-input" id="unlimiteddate" name="unlimiteddate" onchange="toggleCheckbox(this)" <#if dbvaliduntildate == "3020-01-01">checked</#if>>
                    <label class="custom-control-label" for="unlimiteddate">Бессрочно</label>
                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-9">
                        <#if document.parameters??>
                            <input type="text" class="form-control" name="parameters" placeholder="Реквизиты" value="${document.parameters}"/>
                        <#else>
                            <input type="text" class="form-control" name="parameters" placeholder="Реквизиты"/>
                        </#if>
                    </div>
                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-9">
                        <#if document.link??>
                            <input type="text" class="form-control" name="link" placeholder="Ссылка" value="${document.link}"/>
                        <#else>
                            <input type="text" class="form-control" name="link" placeholder="Ссылка"/>
                        </#if>
                    </div>
                </div>

            <br>
                <#if document.documentfilename??>
                    <div>
                    Вложение : ${document.documentfilename}
                    <br>
                    <!--<button class="btn btn-success mt-2" onclick="window.open('http://${hostname}/${urlprefixPath}docs/${document.documentfilename}', '_blank')">-->
                    <button class="btn btn-success mt-2" onclick="openLink(event, 'http://${hostname}/${urlprefixPath}docs/${document.documentfilename}')">
                        Посмотреть вложение
                    </button>
                </div>
                </#if>

            </div>

            <button type="submit" id="submit" class="btn btn-primary mb-2">Изменить данные документа</button>
            <button class="btn btn-secondary mb-2" onclick="window.close()">Выйти без изменения</button>




            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>

<br>


</@c.page>