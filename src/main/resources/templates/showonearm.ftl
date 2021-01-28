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
    <!--
    <div class="form-group row mt-3">
        <div class="col-sm-3">
            <button class="btn btn-primary mb-2" onclick="window.close()">Закрыть</button>

        </div>
    </div>
    -->

    <div class="form-row">
        <div class="form-group col-md-10">
            <form method="post" enctype="multipart/form-data" id="js-upload-form">

                <div><h3 class="display-4">АРМ типа <#if arm?has_content>${arm.armType.name} : ${arm.armName} <#if arm.armNumber?has_content && arm.armNumber != "0"> Номер АРМ : ${arm.armNumber}</#if></#if></h3></div>


                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col"><button class="btn btn-primary mb-2" onclick="window.close(this);window.open('${urlprefixPath}/adddocument?id=${arm.id}&type=arm')">Добавить документ</button></th>
                        <th scope="col"><button class="btn btn-secondary mb-2" onclick="window.close()">Выйти без изменения</button></th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr class="table-light">

                    </tr>

                    </tbody>
                </table>
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col"><button class="btn btn-success mb-2" onclick="window.close(this);window.open('${urlprefixPath}/addkcforarm?id=${arm.id}&type=arm')">Добавить ключевые носители</button></th>
                        <th scope="col"><button class="btn btn-success mb-2" onclick="window.close(this);window.open('${urlprefixPath}/addsziforarm?id=${arm.id}&type=arm<#if sourcetype?? && sourceid??>&sourcetype=${sourcetype}&sourceid=${sourceid}</#if>')">Добавить СЗИ</button></th>
                        <th scope="col"><button class="btn btn-success mb-2" onclick="window.close(this);window.open('${urlprefixPath}/addskziforarm?id=${arm.id}&type=arm')">Добавить СКЗИ</button></th>
                        <th scope="col"><button class="btn btn-success mb-2" onclick="window.close(this);window.open('${urlprefixPath}/addsystemforarm?id=${arm.id}&type=arm')">Добавить Системы</button></th>
                        <#if !arm.antivirus??>
                        <th scope="col"><button class="btn btn-success mb-2" onclick="window.close(this);window.open('${urlprefixPath}/addavforarm?id=${arm.id}&type=arm')">Добавить Антивирус</button></th>
                        </#if>
                        <#if !arm.pakUC?? && arm.armType.id == 1>
                        <th scope="col"><button class="btn btn-success mb-2" onclick="window.close(this);window.open('${urlprefixPath}/addpakucforarm?id=${arm.id}&type=arm')">Добавить Пак УЦ</button></th>
                        </#if>

                    </tr>
                    </thead>
                    <tbody>

                    <tr class="table-light">

                    </tr>

                    </tbody>
                </table>



                <br>
                <#if arm.antivirus??>
                <div class="jumbotron">
                    <h4 class="display-5">Антивирус</h4>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Наименование Антивируса</th>
                            <th scope="col">Версия Антивируса</th>
                            <th scope="col">Действие</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list antivirusList as antivirus>

                            <tr class="table-light">

                                <td onclick="window.open('${urlprefixPath}/showoneantivirus?id=${antivirus.id}')"><#if antivirus.avName??>${antivirus.avName}</#if></td>
                                <td><#if antivirus.avVersion??>${antivirus.avVersion}</#if></td>
                                <td><#if antivirus??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${antivirus.id}&type=antivirus&returntype=arm&returnid=${arm.id}')"></#if></td>

                            </tr>
                        <#else>
                            Пусто
                        </#list>
                        </tbody>
                    </table>
                </div>
                </#if>
                   <#if arm.pakUC??>
                       <div class="jumbotron">
                        <h4 class="display-5">ПАК УЦ</h4>
                        <table class="table">
                            <thead>
                            <tr>
                                <th scope="col">Наименование ПАК УЦ</th>
                                <th scope="col">Действие</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list pakUCList as pakUC>

                                <tr class="table-light">

                                    <td  onclick="window.open('${urlprefixPath}/showonepakuc?id=${pakUC.id}')"><#if pakUC.pakUCName??>${pakUC.pakUCName}</#if></td>
                                    <td><#if pakUC??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${pakUC.id}&type=pakuc&returntype=arm&returnid=${arm.id}')"></#if></td>

                                </tr>
                            <#else>
                                Пусто
                            </#list>
                            </tbody>
                        </table>
                </div>
                    <br>
                    </#if>

                    <div class="jumbotron">
                        <h4 class="display-5">СЗИ</h4>
                        <table class="table">
                            <thead>
                            <tr>
                                <th scope="col">Наименование СЗИ</th>
                                <th scope="col">Уровень</th>
                                <th scope="col">Действие</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list szis as szi>

                                <tr class="table-light">

                                    <td  onclick="window.open('${urlprefixPath}/showoneszi?id=${szi.id}')"><#if szi.sziName??>${szi.sziName}</#if></td>
                                    <td><#if szi.KS2?has_content><#if szi.KS2 != "0">КС2</#if></#if></td>
                                    <td><#if szi??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${szi.id}&type=szi&returntype=arm&returnid=${arm.id}')"></#if></td>

                                </tr>
                            <#else>
                                Пусто
                            </#list>
                            </tbody>
                        </table>
                    </div>

                <div class="jumbotron">
                    <h4 class="display-5">СКЗИ</h4>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Наименование СКЗИ</th>
                            <th scope="col">Версия СКЗИ</th>
                            <th scope="col">Вариант исполнения</th>
                            <th scope="col">Уровень</th>
                            <th scope="col">Действие</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list skzis as skzi>

                            <tr class="table-light">

                                <td  onclick="window.open('${urlprefixPath}/showoneskzi?skziid=${skzi.id}')"><#if skzi.name??>${skzi.name}</#if></td>
                                <td><#if skzi.version??>${skzi.version}</#if></td>
                                <td><#if skzi.realizationVariant??>${skzi.realizationVariant}</#if></td>
                                <td><#if skzi.KS??><#if skzi.KS == "KS1">КС1<#elseif skzi.KS == "KS2">КС2</#if></#if></td>
                                <td><#if skzi??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${skzi.id}&type=skzi&returntype=arm&returnid=${arm.id}')"></#if></td>

                            </tr>
                        <#else>
                            Пусто
                        </#list>
                        </tbody>
                    </table>
                </div>

                <div class="jumbotron">
                    <h4 class="display-5">Ключевые носители АРМ</h4>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Наименование Ключевых носителей</th>
                            <th scope="col">Тип Ключевых носителей</th>
                            <th scope="col">Действие</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list kcs as keyCarriers>

                            <tr class="table-light">

                                <td onclick="window.open('${urlprefixPath}/showonekeycarriers?id=${keyCarriers.id}')"><#if keyCarriers.keyCarriersName??>${keyCarriers.keyCarriersName}</#if></td>
                                <td><#if keyCarriers.keyCarriersType??>${keyCarriers.keyCarriersType.name}</#if></td>
                                <td><#if keyCarriers??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${keyCarriers.id}&type=keycarriers&returntype=arm&returnid=${arm.id}')"></#if></td>

                            </tr>
                        <#else>
                            Пусто
                        </#list>
                        </tbody>
                    </table>
                </div>

                <div class="jumbotron">
                    <h4 class="display-5">Документы АРМ</h4>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Номер документа</th>
                            <th scope="col">Дата документа</th>
                            <th scope="col">Срок действия</th>
                            <th scope="col">Реквизиты</th>
                            <th scope="col">Тип</th>
                            <th scope="col">Действие</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list documents as document>

                            <tr class="table-light">

                                <td onclick="window.open('${urlprefixPath}/showonedocument?docid=${document.id}')"><#if document.documentNumber??>${document.documentNumber}</#if></td>
                                <td>${document.documentDate}</td>
                                <td><#if document.validUntilDate != "01.01.3020">${document.validUntilDate}<#else>Бессрочно</#if></td>
                                <td>${document.parameters}</td>
                                <td>${document.documenttype.name}</td>
                                <td><input type="button" value="Удалить" class="btn btn-danger" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${document.id}&type=document&returntype=arm&returnid=${arm.id}')"></td>


                            </tr>
                        <#else>
                            Пусто
                        </#list>
                        </tbody>
                    </table>
                </div>
                <div class="jumbotron">
                    <h4 class="display-5">Поддерживаемые системы</h4>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Наименование системы</th>
                            <th scope="col">Описание</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list systemnames as systemname>

                            <tr class="table-light">

                                <td><#if systemname.name??>${systemname.name}</#if></td>
                                <td><#if systemname.description??>${systemname.description}</#if></td>


                            </tr>
                        <#else>
                            Пусто
                        </#list>
                        </tbody>
                    </table>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}">

            </form>
        </div>
    </div>

    <br>


</@c.page>