package ${packageName};

public interface ${className} {
<#if listMethod?? && (listMethod?size>0)>
	<#list listMethod as method>
		String ${method.name}(
		<#if method.args?? && (method.args?size>0)>
			<#list method.args as arg>
				${arg} arg${arg?counter}<#sep>, </#sep>
			</#list>
		</#if>
		);
	</#list>
</#if>
}