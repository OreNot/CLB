<#include "security.ftl">
<#import "login.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
     <a class="navbar-brand" href="${urlprefixPath}/showreports">CLB</a>
        <!--<a class="navbar-brand" href="$prefix$/">UC</a>  -->
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">


    <#if isOperator || isManager || isAdmin>
            <li class="nav-item">
                 <a class="nav-link" href="${urlprefixPath}/addtoarchive">Добавление в Архив</a>
            </li>
                <li class="nav-item">
             <a class="nav-link" href="${urlprefixPath}/showarchive">Просмотр Архива</a>
                </li>

                </#if>



            <#if isManager || isAdmin || isGroupBoss>
                <li class="nav-item">
                     <a class="nav-link" href="${urlprefixPath}/addtask">Добавить задачу</a>
                </li>

                </#if>
<#if isManager || isSuperBoss || isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="${urlprefixPath}/showtask">Задачи</a>
                </li>


</#if>

    <#if isGroupBoss>


        <li class="nav-item">
            <a class="nav-link" href="${urlprefixPath}/myrgtasks">Задачи на мою РГ</a>
        </li>
    </#if>

    <#if isManager || isSuperBoss || isAdmin>

        <li class="nav-item">
            <a class="nav-link" href="${urlprefixPath}/superbossdashboard">Статистика</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${urlprefixPath}/taskfounder">Поиск задач</a>
        </li>


    </#if>
    <#if isAdmin>
    <li class="nav-item">
             <a class="nav-link" href="${urlprefixPath}/usersettings">Настройки пользователей</a>
    </li>
 </#if>
<#if isUser>

    <li class="nav-item">
        <a class="nav-link" href="${urlprefixPath}/showreports">Заключения</a>
    </li>

        <li class="nav-item">
            <a class="nav-link" href="${urlprefixPath}/showbanks">Банки</a>
        </li>

        <li class="nav-item">
            <a class="nav-link" href="${urlprefixPath}/showorganizations">Организации</a>
        </li>

    <li class="nav-item">
    <a class="nav-link" href="${urlprefixPath}/showsystems">Системы</a>
</li>

    <li class="nav-item">
        <a class="nav-link" href="${urlprefixPath}/showskzis">СКЗИ</a>
    </li>

    <li class="nav-item">
        <a class="nav-link" href="${urlprefixPath}/showszis">СЗИ</a>
    </li>

    <li class="nav-item">
        <a class="nav-link" href="${urlprefixPath}/showantiviruses">Антивирусы</a>
    </li>

    <li class="nav-item">
        <a class="nav-link" href="${urlprefixPath}/showkcs">Ключевые носители</a>
    </li>

    <li class="nav-item">
        <a class="nav-link" href="${urlprefixPath}/showpakcs">ПАК УЦ</a>
    </li>


</#if>
    <#if isAdmin || isOperator>
        <li class="nav-item">
            <a class="nav-link" href="${urlprefixPath}/archivelog">Лог архива</a>
        </li>
    </#if>
        </ul>

        <div class="navbar-text mr-3">${fio}</div>
    <@l.logout/>
    </div>
</nav>