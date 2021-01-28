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
                <h4 class="display-4">Добавление Систем для <#if type == "arm">АРМ </#if><#if type == "bank">банка </#if><#if arm??>${arm.armName}</#if><#if organization??>${organization.name}</#if></h4>
               <br>
                <div class="jumbotron">
                    <h4 class="display-5">Поддерживаемые системы</h4>
                    <#list armsystemnames as armsystemname>

                        <div class="custom-control custom-checkbox">
                            <input type="checkbox" class="custom-control-input" id="${armsystemname.id}" value="${armsystemname.id}" name="sysids" checked>
                            <label class="custom-control-label" for="${armsystemname.id}" >${armsystemname.name}</label>
                        </div>
                        <br>
                    <#else>
                        Пусто
                    </#list>

                    <br>
                    <h4 class="display-5">Cистемы</h4>
                    <#list armnotsystemnames as armnotsystemname>

                        <div class="custom-control custom-checkbox">
                            <input type="checkbox" class="custom-control-input" id="${armnotsystemname.id}" value="${armnotsystemname.id}" name="sysids">
                            <label class="custom-control-label" for="${armnotsystemname.id}" >${armnotsystemname.name}</label>
                        </div>
                        <br>
                    <#else>
                        Пусто
                    </#list>

                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button type="submit" class="btn btn-primary mb-2">Добавить Систему</button>

                    </div>
                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button class="btn btn-secondary mb-2" onclick="window.close()">Выйти без изменения</button>
                    </div>
                </div>

                <input type="hidden" name="_csrf" value="${_csrf.token}">

            </form>
        </div>
    </div>
    <br>


</@c.page>