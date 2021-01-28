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
                <h4 class="display-4">Добавление ПАК УЦ</h4>
                <div class="jumbotron">
                    <div class="form-group row mt-3">
                        <div class="col-sm-9">
                            <#if pakucname??>
                                <input type="text" class="form-control" name="pakucname" placeholder="Наименование ПАК УЦ" value="${pakucname}"/>
                            <#else>
                                <input type="text" class="form-control" name="pakucname" placeholder="Наименование ПАК УЦ"/>
                            </#if>
                        </div>
                    </div>


                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button type="submit" class="btn btn-primary mb-2">Добавить ПАК УЦ</button>

                    </div>
                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button class="btn btn-secondary mb-2" onclick="window.open('${urlprefixPath}/showpakcs');window.close(this)">Выйти без изменения</button>
                    </div>
                </div>

                <input type="hidden" name="_csrf" value="${_csrf.token}">

            </form>
        </div>
    </div>
    <br>


</@c.page>