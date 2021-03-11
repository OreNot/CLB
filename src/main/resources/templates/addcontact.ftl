<#import "parts/common.ftl" as c>

<@c.page>

<br>
    <script src="/js/scripts.js"></script>

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
            <h4 class="display-4">Добавление Контакта</h4>
            <div class="jumbotron">
                <h4 class="display-5">Информация о контатке</h4>
            <div class="form-group row mt-3">
                <div class="col-sm-9">
                        <input type="text" class="form-control" name="fio" placeholder="Ф.И.О."/>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                        <input type="text" class="form-control" name="position" placeholder="Должность"/>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                        <input type="text" class="form-control" name="phonenumber" placeholder="Тел. номер"/>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                        <input type="text" class="form-control" name="email" placeholder="E-mail"/>
                </div>
            </div>
            </div>

            <!--
        <div class="form-group row mt-3">
            <div class="col-sm-9">
                <#if fio??>
                <input type="text" class="form-control" name="fio" placeholder="ФИО" value="${fio}"/>
                <#else>
                    <input type="text" class="form-control" name="fio" placeholder="ФИО"/>
                </#if>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-sm-9">
                <#if organization??>
                <input type="text" class="form-control" name="organization" placeholder="Организация" value="${organization}"/>
                    <#else>
                        <input type="text" class="form-control" name="organization" placeholder="Организация"/>
                </#if>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-sm-3">
                <#if catnum??>
                <input type="text" class="form-control" name="catnum" placeholder="Номер папки" value="${catnum}">
                <#else>
                    <input type="text" class="form-control" name="catnum" placeholder="Номер папки">
                </#if>
            </div>
        </div>

        <div class="file-upload-wrapper mb-2">
            <input type="file"  name="file" id="input-file-now" class="file-upload"/>
            <label class="input-file-label" for="input-file-now">Документы</label>
        </div>
-->
            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button type="submit" class="btn btn-primary mb-2">Добавить контактное лицо</button>

                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-secondary mb-2" onclick="window.close();window.open('${urlprefixPath}/showone${type}?id=${id}')">Выйти без изменения</button>
                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>
<br>


</@c.page>