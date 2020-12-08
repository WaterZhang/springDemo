package com.zhangzemiao.www.springdemo.domain.feign.plugin;

import static com.netflix.hystrix.HystrixCommandKey.Factory.asKey;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesCommandDefault;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Feign8CompatiblePropertiesStrategy extends HystrixPropertiesStrategy {
    //private static Logger logger = LoggerFactory.getLogger(Feign8CompatiblePropertiesStrategy.class);
    private static final Pattern COMMAND_KEY_PATTERN = Pattern.compile("\\w+[#](\\w+)[(].*[)]");

    /**
     * Feign 9: eventName=RequestContextServiceConnector#getEPCContext(String,String,String,String)
     * Feign 8: eventName=getEPCContext
     *
     * @param commandKey
     * @param builder
     * @return
     */
    @Override
    public HystrixCommandProperties getCommandProperties(final HystrixCommandKey commandKey,
                                                         final HystrixCommandProperties.Setter builder) {
        HystrixCommandProperties commandProperties = super.getCommandProperties(commandKey, builder);

        if (!ConfigurationManager.getConfigInstance().getKeys("hystrix.command." + commandKey.name()).hasNext()) {
            final Matcher matcher = COMMAND_KEY_PATTERN.matcher(commandKey.name());
            if (matcher.matches()) {
                final String commandKeyName = matcher.group(1);
                if (ConfigurationManager.getConfigInstance().getKeys("hystrix.command." + commandKeyName).hasNext()) {
                    commandProperties = new HystrixPropertiesCommandDefault(asKey(commandKeyName), builder);
                }
            }
        }
        return commandProperties;
    }
}
