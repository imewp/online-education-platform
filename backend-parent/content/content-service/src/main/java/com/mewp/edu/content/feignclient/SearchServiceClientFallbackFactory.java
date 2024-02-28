package com.mewp.edu.content.feignclient;

import com.mewp.edu.content.model.dto.CourseIndex;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/28 14:10
 */
@Slf4j
@Component
public class SearchServiceClientFallbackFactory implements FallbackFactory<SearchServiceClient> {
    @Override
    public SearchServiceClient create(Throwable throwable) {
        return new SearchServiceClient() {
            @Override
            public Boolean add(CourseIndex courseIndex) {
                log.debug("调用搜索发生熔断走降级方法,熔断异常:{}", throwable.getMessage(), throwable);
                return false;
            }
        };
    }
}

