package effectivejava.chapter05.item33;

import org.springframework.core.ParameterizedTypeReference;

public interface RequestService {

    <T> T exchange(String url, Object httpMethod, Object httpHeaders, Object params,
                   ParameterizedTypeReference<T> responseType, boolean isLog,
                   String...uriVariables);
}
