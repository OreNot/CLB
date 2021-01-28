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
                <h4 class="display-4">Добавление СЗИ</h4>
                <div class="jumbotron">
                    <div class="form-group row mt-3">
                        <div class="col-sm-9">
                            <#if sziname??>
                                <input type="text" class="form-control" name="sziname" placeholder="Наименование СЗИ" value="${sziname}"/>
                            <#else>
                                <input type="text" class="form-control" name="sziname" placeholder="Наименование СЗИ"/>
                            </#if>
                        </div>
                    </div>
                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input" value="KS2" id="kslevel" name="kslevel">
                        <label class="custom-control-label" for="kslevel">КС2</label>
                    </div>

                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button type="submit" class="btn btn-primary mb-2">Добавить СЗИ</button>

                    </div>
                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button class="btn btn-secondary mb-2" onclick="window.open('${urlprefixPath}/showszis');window.close(this)">Выйти без изменения</button>
                    </div>
                </div>

                <input type="hidden" name="_csrf" value="${_csrf.token}">

            </form>
        </div>
    </div>
    <br>


</@c.page>