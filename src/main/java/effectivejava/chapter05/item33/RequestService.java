package effectivejava.chapter05.item33;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public interface RequestService {

    <T> T exchange(String url, HttpMethod httpMethod, HttpHeaders httpHeaders, Object params,
                   ParameterizedTypeReference<T> responseType, boolean isLog,
                   String...uriVariables);
}
