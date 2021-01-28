<#import "parts/common.ftl" as c>

<@c.page>

<br>

    <script>
        function genPaper() {

            var skzisource = document.getElementById("bankskziinfo");
            var banknotgetinformation = document.getElementById("banknotgetinformation");

            if (skzisource.checked == true && banknotgetinformation.checked == true){
                window.close(this);
                window.open('${urlprefixPath}/generatepaper?reportid=${report.id}&skzisource=' + skzisource.value + '&banknotgetinformation=' + banknotgetinformation.value);
            }
            else if (skzisource.checked == false && banknotgetinformation.checked == true)
            {
                window.close(this);
                window.open('${urlprefixPath}/generatepaper?reportid=${report.id}&skzisource=0&banknotgetinformation=' + banknotgetinformation.value);
            }
            else if (skzisource.checked == true && banknotgetinformation.checked == false)
            {
                window.close(this);
                window.open('${urlprefixPath}/generatepaper?reportid=${report.id}&skzisource=' + skzisource.value + '&banknotgetinformation=0');
            }
            else
            {
                window.close(this);
                window.open('${urlprefixPath}/generatepaper?reportid=${report.id}&skzisource=0&banknotgetinformation=0');
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

            <div><h3 class="display-4">Заключение № <#if report?has_content>${report.documentnumber}</#if></h3></div>

            <div class="jumbotron">
                <h4 class="display-5">Текущая информация</h4>
            <div class="form-check form-check-inline mt-3">
                <label for="reportstatusid">Статус заключения</label>
                <select class="form-control" name="reportstatusname" id="reportstatusname" placeholder="Статус заключения">
                    <option value="Статус заключения">Статус заключения</option>
                    <#if statuses??>
                        <#list statuses as status>
                            <option <#if report.status.name?has_content && report.status.name  == "${status.name}">selected</#if> value="${status.name}">${status.name}</option>
                        </#list>
                    </#if>

                </select>

            </div>

            <br>

            <div class="form-group row mt-3">
                <div class="col-md-10">
                    <label for="сhronos">Хронология</label>
                    <textarea readonly class="form-control" name="сhronos"  id="сhronos" rows="5" style="min-width: 100%"><#if report.contactlog?has_content>${report.contactlog}<#else>Лог пока пуст</#if></textarea>
                </div>
            </div>
            <div class="form-group row mt-3">
                <div class="col-sm-9">
                        <input type="text" class="form-control" name="addtochronos" placeholder="Добавить в хронологию"/>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button type="submit" class="btn btn-primary mb-2">Обновить документ</button>

                </div>
            </div>


                <div class="jumbotron">
                    <h4 class="display-5">Работа с бумажными носителями</h4>

                        <h5 class="display-6">Кто передал СКЗИ</h5>


                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input" value="bankskziinfo" id="bankskziinfo" name="bankskziinfo">
                        <label class="custom-control-label" for="bankskziinfo">СКЗИ получено от банка</label>
                    </div>

                        <div class="custom-control custom-checkbox">
                            <input type="checkbox" class="custom-control-input" value="banknotgetinformation" id="banknotgetinformation" name="banknotgetinformation">
                            <label class="custom-control-label" for="banknotgetinformation">Банк не предоставил информацию</label>
                        </div>


                    <div class="form-group row mt-3">
                        <div class="col-sm-3">
                            <!--<button class="btn btn-success mb-2" onclick="window.close(this);window.open('${urlprefixPath}/generatepaper?reportid=${report.id}')">Сформировать новый экземпляр</button>-->
                            <button class="btn btn-success mb-2" onclick="genPaper()">Сформировать новый экземпляр</button>
                        </div>
                    </div>

                    <#if paperreporterror??>

                        <small id="validuntildateHelp" class="form-text text-muted">*Для формирования документа на бумажном носителе необходимо наличие следующих данных:</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Договор между ГА и организацией с заполненными реквизитами (Клиент);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Заключение органа криптографической защиты о возможности эксплуатации СКЗИ (Клиент, Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Договор между клиентом и банком (Клиент);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Лицензия на использование средства, реализующего инфраструктуру ключевой системы (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted"> или</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Договор на использование средства, реализующего инфраструктуру ключевой системы (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Сертификат соответствия ФСБ России на СКЗИ, использующееся на рабочих местах пользователей, с классом не ниже КС1 (Клиент, Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted"> или</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Сертификат соответствия ФСБ России на СКЗИ, использующееся на рабочих местах пользователей, с классом не ниже КС2 (Клиент, Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Заполненные данные о банке, организации, их СКЗИ и системе;</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Договор на использование СКЗИ, использующегося в составе средства, реализующего инфраструктуру ключевой системы (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Договор, подтверждающий право передачи СКЗИ Клиенту, использующегося в работе Системы (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Лицензия ФСБ России на соответствующие виды деятельности (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Лицензия на использование СКЗИ, использующегося в составе средства, реализующего инфраструктуру ключевой системы (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Лицензия на передачу Клиенту СКЗИ для работы в Системе (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Лицензия на программное обеспечение Системы (Система);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Сертификат соответствия ФСБ России на средство, реализующие инфраструктуру ключевой системы по классу не ниже КС2 (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Сертификат соответствия ФСБ России на СКЗИ, использующееся для работы средства, реализующего инфраструктуру ключевой системы с классом защиты не ниже КС2 (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Сертификат соответствия ФСБ России на ключевые носители (Клиент, Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Сертификат соответствия ФСБ России на СКЗИ, использующееся на рабочих местах пользователей (Клиент, Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Сертификат соответствия ФСТЭК России на ключевые носители (Клиент, Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Сертификат соответствия ФСТЭК России на антивирусное ПО, установленное на АРМ (сервере), где функционирует средство реализующие инфраструктуру ключевой системы (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Сертификат соответствия ФСТЭК России на антивирусное ПО, установленное на АРМ пользователей (Клиент, Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Сертификат соответствия ФСТЭК России на СЗИ от НСД на АРМ (сервере), где функционирует средство реализующие инфраструктуру ключевой системы (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Сертификат соответствия ФСТЭК России на СЗИ от НСД на АРМ пользователей (Клиент, Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Заключение о корректности встраивания СКЗИ в Систему (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Заключение о сдаче зачетов пользователями СКЗИ (Клиент, Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Приказ о допуске пользователей к самостоятельной работе с СКЗИ (Клиент, Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Приказ о правах пользователей в Системе (Клиент, Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Регламент удостоверяющего центра (Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Регламент органа криптографической защиты (Клиент, Банк);</small>
                        <small id="validuntildateHelp" class="form-text text-muted">- Отчет по форме 382 П (Банк);</small>
                    </#if>

<!--                <small id="validuntildateHelp" class="form-text text-muted">*Для формирования документа на бумажном носителе необходимо заполнить информацию о письмах</small>

                <h5 class="display-7">Письмо о результатах оценки уровня доверия к автоматизированной системе О результатах оценки уровня доверия к автоматизированной системе ${report.system.name} ${report.bank.name}</h5>
                <div class="form-group row mt-3 ml-3">
                    <div class="row ">
                    <div class="col-xs-3">
                            <input type="text" class="form-control" name="lettertobanknumber" placeholder="№ Письма"/>
                    </div>
                        <div class="col-xs-3">
                            <input type="date" class="form-control" id="lettertobankdate" name="lettertobankdate" placeholder="Дата письма"/>
                        </div>
                    </div>

                </div>

                <h5 class="display-7">Письмо от ${report.bank.name}</h5>
                <div class="form-group row mt-3 ml-3">
                    <div class="row">
                        <div class="col-xs-3">
                            <input type="text" class="form-control" name="letterаfrombanknumber" placeholder="№ Письма"/>
                        </div>
                        <div class="col-xs-3">
                            <input type="date" class="form-control" id="letterfrombankdate" name="lettertobankdate" placeholder="Дата письма"/>
                        </div>
                    </div>
                </div>
-->



                    <#if report.documentfilename??>
                        Файл заключения : ${report.documentfilename}
                        <br>

                        <a class="btn btn-success mt-2" role="button" href="http://${hostname}${urlprefixPath}/reports/${report.documentfilename}"
                           download="${report.documentfilename}">
                            Посмотреть вложение
                        </a>

                    </#if>
                </div>


            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-secondary mb-2" onclick="window.close()">Выйти без изменения</button>
                </div>
            </div>
            </div>

            <br>
                <div class="jumbotron">
                    <h4 class="display-5">Информация о заключении</h4>
            <div>

            Номер Документа : <#if report.documentnumber??>${report.documentnumber}<#else>Не присвоен</#if>

            </div>
                </div>



            <br>

            <div>

                Дата документа : <#if report.documentdate??>${report.documentdate}<#else>Не присвоена</#if>

            </div>


            <div class="form-group row mt-3">
                <div class="col-auto">
                    <label for="validuntildate"><#if danger?has_content><p class="text-danger">Срок действия</p><#else>Срок действия</#if></label>
                    <#if dbvaliduntildate??>
                        <input type="date" class="form-control" id="validuntildate" name="validuntildate" placeholder="Срок действия" value="${dbvaliduntildate}"/>
                    <#else>
                        <input type="date" class="form-control" id="validuntildate" name="validuntildate" placeholder="Срок действия"/>
                    </#if>
                    <#if danger?has_content><small id="validuntildateHelp" class="form-text text-muted">До истечения срока действия осталось менее трёх дней</small></#if>
                </div>
            </div>

            <br>

            <div>

                Реквизиты : <#if report.parameters??>${report.parameters}<#else>Не присвоены</#if>

            </div>

            <br>

            <div>

                Клиент : <#if report.organization.name??>${report.organization.name}<#else>Не присвоен</#if>

            </div>


            <br>

            <div>

                Банк : <#if report.bank.name??>${report.bank.name}<#else>Не присвоен</#if>

            </div>

            <br>

            <div>

                Система : <#if report.system.name??>${report.system.name}<#else>Не присвоена</#if>

            </div>

            <br>

            <div class="form-check form-check-inline mt-3">
                <label for="reportsystemstatusname">Статус системы</label>
                <select class="form-control" name="reportsystemstatusname" id="reportsystemstatusname" placeholder="Статус системы">
                    <option value="Статус системы">Статус системы</option>
                    <#if systemstatuses??>
                        <#list systemstatuses as systemstatus>
                            <option <#if report.systemstatus.name?has_content && report.systemstatus.name  == "${systemstatus.name}">selected</#if> value="${systemstatus.name}">${systemstatus.name}</option>
                        </#list>
                    </#if>

                </select>

            </div>
                </div>

            <br>

            <#if recommendations??>
            <div class="jumbotron">
                <h4 class="display-4">Рекомендации</h4>
                ${recommendations}
            </div>
            </#if>

            <!--
            <div class="form-check form-check-inline mt-3">

                <select class="form-control" name="status" id="status" placeholder="Статус">
                    <option value="Статус Документа">Статус Документа</option>
                    <#list statuses as status>
                        <option>${status.name}</option>
                    </#list>

                </select>

            </div>
            -->
            <br>


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


            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>
<br>


</@c.page>