<#import "parts/common.ftl" as c>

<@c.page>

    <br>
    <script src="/js/scripts.js"></script>
    <script language="JavaScript">

        function toggleRadio(elem) {

            if(elem.id == "user" && elem.checked) {
                document.getElementById('pakucdiv').hidden = true;
                document.getElementById('armnumberdiv').hidden = false;
            }
            if (elem.id == "server" && elem.checked)
            {
                document.getElementById('pakucdiv').hidden = false;
                document.getElementById('armnumberdiv').hidden = true;
            }
        }

        function toggleSelect(elem) {

            document.getElementById("submit").click();
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
                <h4 class="display-4">Добавление АРМ для <#if type == "bank">банка </#if>${organization.name}</h4>
                <div class="jumbotron">
                    <h4 class="display-5">Параметры АРМ</h4>
                    <div class="btn-group btn-group-toggle" data-toggle="buttons">
                        <label class="btn btn-secondary active">
                            <input type="radio" name="armtype"  value="2" id="user" autocomplete="off" checked onchange="toggleRadio(this)"> Пользователь
                        </label>
                        <label class="btn btn-secondary">
                            <input type="radio" name="armtype" value="1" id="server" autocomplete="off" onchange="toggleRadio(this)"> Сервер
                        </label>
                    </div>

                    <div class="form-group row mt-3">
                        <div class="col-sm-9">
                            <#if armname??>
                                <input type="text" class="form-control" name="armname" placeholder="Наименование АРМ" value="${armname}"/>
                            <#else>
                                <input type="text" class="form-control" name="armname" placeholder="Наименование АРМ"/>
                            </#if>
                        </div>
                    </div>
                    <#if type == "organization">
                    <div class="form-group row mt-3" id="armnumberdiv">
                        <div class="col-sm-9">
                            <#if armnumber??>
                                <input type="text" class="form-control" name="armnumber" placeholder="Номер АРМ" value="${armnumber}"/>
                            <#else>
                                <input type="text" class="form-control" name="armnumber" placeholder="Номер АРМ"/>
                            </#if>
                        </div>
                    </div>
                    </#if>
                    <div class="form-check form-check-inline mt-3">

                        <select class="form-control" style="width:auto;" name="antivirus" id="antivirus" placeholder="Антивирус">
                            <option value="0">Антивирус</option>
                            <#list antiviruses as antivirus>
                                <option value="${antivirus.id}" <#if selectedantivirus??><#if antivirus.avName == "${selectedantivirus}">selected="selected"</#if></#if>>${antivirus.avName}</option>
                            </#list>

                        </select>

                    </div>

                    <div class="form-check form-check-inline mt-3">

                        <select class="form-control" style="width:auto;" name="szi" id="szi" placeholder="СЗИ">
                            <option value="0">СЗИ</option>
                            <#list szis as szi>
                                <option value="${szi.id}" <#if selectedszi??><#if szi.sziName == "${selectedszi}">selected="selected"</#if></#if>>${szi.sziName}</option>
                            </#list>

                        </select>

                    </div>

                    <div class="form-check form-check-inline mt-3" id="pakucdiv" hidden>

                        <select class="form-control" style="width:auto;" name="pakuc" id="pakuc" placeholder="ПАК УЦ">
                            <option value="0">ПАК УЦ</option>
                            <#list pakucs as pakuc>
                                <option value="${pakuc.id}" <#if selectedpakuc??><#if pakuc.pakUCName == "${selectedpakuc}">selected="selected"</#if></#if>>${pakuc.pakUCName}</option>
                            </#list>

                        </select>

                    </div>

                    <div class="form-check form-check-inline mt-3">

                        <select class="form-control" style="width:auto;" name="skzi" id="skzi" placeholder="СКЗИ">
                            <option value="0">СКЗИ</option>
                            <#list skzis as skzi>
                                <option value="${skzi.id}" <#if selectedskzi??><#if skzi.name == "${selectedskzi}">selected="selected"</#if></#if>>${skzi.name} ${skzi.version} ${skzi.realizationVariant} ${skzi.KS}</option>
                            </#list>

                        </select>

                    </div>

                    <div class="form-check form-check-inline mt-3">

                        <select class="form-control" style="width:auto;" name="kc" id="kc" placeholder="Ключевые носители">
                            <option value="0">Ключевые носители</option>
                            <#list keycarriers as keycarrier>
                                <option value="${keycarrier.id}" <#if selectedkeycarrier??><#if keycarrier.keyCarriersName == "${selectedkeycarrier}">selected="selected"</#if></#if>>${keycarrier.keyCarriersName}</option>
                            </#list>

                        </select>

                    </div>

                </div>

                <div class="jumbotron">
                    <h4 class="display-5">Поддерживаемые системы</h4>
                        <#list systemnames as systemname>

                    <div class="custom-control custom-checkbox">
                                <input type="checkbox" class="custom-control-input" id="${systemname.id}" value="${systemname.id}" name="sysids">
                                <label class="custom-control-label" for="${systemname.id}" >${systemname.name}</label>
                    </div>
                            <br>

                        </#list>

                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button type="submit" class="btn btn-primary mb-2">Добавить АРМ</button>

                    </div>
                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button class="btn btn-secondary mb-2" onclick="window.open('${urlprefixPath}/<#if type == "bank">showonebank<#elseif type == "organization">showoneorganization</#if>?id=${id}');window.close(this)">Выйти без изменения</button>
                    </div>
                </div>

                <input type="hidden" name="_csrf" value="${_csrf.token}">

            </form>
        </div>
    </div>
    <br>


</@c.page>