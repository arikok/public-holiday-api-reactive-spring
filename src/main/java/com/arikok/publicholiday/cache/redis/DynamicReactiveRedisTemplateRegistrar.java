package com.arikok.publicholiday.cache.redis;

import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class DynamicReactiveRedisTemplateRegistrar {

  private final ReactiveRedisConnectionFactory factory;
  private final GenericApplicationContext context;

  public DynamicReactiveRedisTemplateRegistrar(ReactiveRedisConnectionFactory factory,
      GenericApplicationContext context) {
    this.factory = factory;
    this.context = context;
  }

  public <T> ReactiveRedisTemplate<String, T> getTemplate(Class<T> target) {
    String beanName = generateBeanName(target);
    if (context.containsBean(beanName)) {
      @SuppressWarnings("unchecked")
      ReactiveRedisTemplate<String, T> template =
          (ReactiveRedisTemplate<String, T>) context.getBean(beanName, ReactiveRedisTemplate.class);
      return template;
    }

    StringRedisSerializer keySerializer = new StringRedisSerializer();
    Jackson2JsonRedisSerializer<T> valueSerializer = new Jackson2JsonRedisSerializer<>(target);
    RedisSerializationContext.RedisSerializationContextBuilder<String, T> builder =
        RedisSerializationContext.newSerializationContext(keySerializer);
    RedisSerializationContext<String, T> contextForType = builder.value(valueSerializer).build();
    ReactiveRedisTemplate<String, T> template = new ReactiveRedisTemplate<>(factory,
        contextForType);

    context.registerBean(beanName, ReactiveRedisTemplate.class, () -> template);
    return template;
  }

  private <T> String generateBeanName(Class<T> target) {
    return "reactiveRedisTemplate" + target.getSimpleName();
  }
}
