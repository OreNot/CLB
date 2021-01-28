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
                <h4 class="display-4">Добавление антивируса</h4>
                <div class="jumbotron">
                    <div class="form-group row mt-3">
                        <div class="col-sm-9">
                            <#if avname??>
                                <input type="text" class="form-control" name="avname" placeholder="Наименование антивируса" value="${avname}"/>
                            <#else>
                                <input type="text" class="form-control" name="avname" placeholder="Наименование антивируса"/>
                            </#if>
                        </div>
                    </div>

                    <div class="form-group row mt-3">
                        <div class="col-sm-9">
                            <#if avversion??>
                                <input type="text" class="form-control" name="avversion" placeholder="Версия" value="${avversion}"/>
                            <#else>
                                <input type="text" class="form-control" name="avversion" placeholder="Версия"/>
                            </#if>
                        </div>
                    </div>


                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button type="submit" class="btn btn-primary mb-2">Добавить антивирус</button>

                    </div>
                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button class="btn btn-secondary mb-2" onclick="window.open('${urlprefixPath}/showantiviruses');window.close(this)">Выйти без изменения</button>
                    </div>
                </div>

                <input type="hidden" name="_csrf" value="${_csrf.token}">

            </form>
        </div>
    </div>
    <br>


</@c.page>