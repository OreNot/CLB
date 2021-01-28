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
            <h4 class="display-4">Добавление СКЗИ</h4>
            <div class="jumbotron">
                <h4 class="display-5">Реквизиты</h4>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if skziname??>
                        <input type="text" class="form-control" name="skziname" placeholder="Наименование СКЗИ" value="${skziname}"/>
                    <#else>
                        <input type="text" class="form-control" name="skziname" placeholder="Наименование СКЗИ"/>
                    </#if>
                </div>
            </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-9">
                        <#if skziversion??>
                            <input type="text" class="form-control" name="skziversion" placeholder="Версия" value="${skziversion}"/>
                        <#else>
                            <input type="text" class="form-control" name="skziversion" placeholder="Версия"/>
                        </#if>
                    </div>
                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-9">
                        <#if skzirealizationvariant??>
                            <input type="text" class="form-control" name="skzirealizationvariant" placeholder="Вариант исполнения" value="${skzirealizationvariant}"/>
                        <#else>
                            <input type="text" class="form-control" name="skzirealizationvariant" placeholder="Вариант исполнения"/>
                        </#if>
                    </div>
                </div>

                <div class="btn-group btn-group-toggle" data-toggle="buttons">
                    <label class="btn btn-secondary active">
                        <input type="radio" name="kslevel"  value="KS1" id="ks1" autocomplete="off" checked> КС1
                    </label>
                    <label class="btn btn-secondary">
                        <input type="radio" name="kslevel" value="KS2" id="ks2" autocomplete="off"> КС2
                    </label>
                </div>

            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button type="submit" class="btn btn-primary mb-2">Добавить СКЗИ</button>

                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-secondary mb-2" onclick="window.open('${urlprefixPath}/showskzis');window.close(this)">Выйти без изменения</button>
                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>
<br>


</@c.page>