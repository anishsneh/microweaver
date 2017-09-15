<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:int="http://www.springframework.org/schema/integration"
    xmlns:int-http="http://www.springframework.org/schema/integration/http"
    xmlns:task="http://www.springframework.org/schema/task"
    xmlns:rabbit="http://www.springframework.org/schema/rabbit"
    xmlns:int-amqp="http://www.springframework.org/schema/integration/amqp"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="     http://www.springframework.org/schema/beans     http://www.springframework.org/schema/beans/spring-beans.xsd     http://www.springframework.org/schema/integration     http://www.springframework.org/schema/integration/spring-integration.xsd     http://www.springframework.org/schema/integration/http     http://www.springframework.org/schema/integration/http/spring-integration-http.xsd     http://www.springframework.org/schema/task http://www.springframework.org/schema/task/spring-task.xsd     http://www.springframework.org/schema/rabbit http://www.springframework.org/schema/rabbit/spring-rabbit.xsd http://www.springframework.org/schema/integration/amqp http://www.springframework.org/schema/integration/amqp/spring-integration-amqp.xsd
    ">
    
    <bean id="jsonMessageConverter" class="org.springframework.amqp.support.converter.Jackson2JsonMessageConverter" />

    <bean
        class="com.anishsneh.microweaver.service.workflow.activator.Starter" id="starter"/>
    <bean
        class="com.anishsneh.microweaver.service.workflow.activator.Terminator" id="terminator"/>
    
    <int-amqp:channel id="requestChannel" connection-factory="connectionFactory" message-driven="true" queue-name="rabbit.requestChannel" message-converter="jsonMessageConverter" extract-payload="true" />
    <rabbit:connection-factory id="connectionFactory" addresses="${r"${spring.rabbitmq.host}"}:${r"${spring.rabbitmq.port}"}" username="${r"${spring.rabbitmq.username}"}" password="${r"${spring.rabbitmq.password}"}" />

    <int-http:inbound-channel-adapter channel="requestChannel"
        path="/execute"
        request-payload-type="com.anishsneh.microweaver.service.workflow.vo.Payload"
        status-code-expression="T(org.springframework.http.HttpStatus).NO_CONTENT" supported-methods="POST">
        <int-http:request-mapping consumes="application/json"/>
    </int-http:inbound-channel-adapter>
    
    <int:service-activator input-channel="requestChannel"
        method="startFlow" output-channel="task1InputChannel" ref="starter" />
    <int:service-activator input-channel="responseChannel"
        method="terminateFlow" output-channel="nullChannel" ref="terminator"/>
      
	<!-- Service activators for workflow tasks -->
	<#list tasks as task>
		<#assign taskCounter = (task?counter)>
		<#assign taskCounterNext = (taskCounter + 1)>
		<#assign tasksSize = (tasks?size)>
		<#if taskCounter != tasksSize>
			<int:service-activator ref="${task.ekey}Bean" method="execute" input-channel="${task.ekey}InputChannel" output-channel="${tasks[taskCounter].ekey}InputChannel" />
		<#else>
			<int:service-activator ref="${task.ekey}Bean" method="execute" input-channel="${task.ekey}InputChannel" output-channel="responseChannel" />
		</#if>
	</#list>
	  
	<!-- Channels for workflow tasks -->
	<#list tasks as task>
		<int-amqp:channel id="${task.ekey}InputChannel" connection-factory="connectionFactory" message-driven="true" queue-name="queue.microweaver.${task.ekey}Queue" message-converter="jsonMessageConverter" extract-payload="true" />
    </#list>
    
    <!-- Bean definitions for workflow tasks -->
    <#list tasks as task>
    		<bean class="com.anishsneh.microweaver.service.workflow.activator.TaskExecutor" id="${task.ekey}Bean">
	    		<constructor-arg value="${task.name}"/>
	    		<constructor-arg value="${task.serviceUri}"/>
	    		<constructor-arg value="${task.serviceMethod}"/>
	    		<constructor-arg value="${task.timeout}"/>
	    </bean>
    </#list>
</beans>