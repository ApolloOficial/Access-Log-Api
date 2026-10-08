package org.apollo.log.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApiKeyFilterTest {

    private MockHttpServletResponse run(ApiKeyFilter filter, String path, String key, MockFilterChain chain)
            throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", path);
        if (key != null) {
            request.addHeader(ApiKeyFilter.HEADER, key);
        }
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, chain);
        return response;
    }

    @Test
    void validKeyPassesThrough() throws Exception {
        MockFilterChain chain = new MockFilterChain();
        MockHttpServletResponse response = run(new ApiKeyFilter("segredo"), "/api/v1/access-logs", "segredo", chain);

        assertEquals(200, response.getStatus());
        assertNotNull(chain.getRequest());
    }

    @Test
    void wrongOrMissingKeyIsRejected() throws Exception {
        MockFilterChain wrongChain = new MockFilterChain();
        MockFilterChain missingChain = new MockFilterChain();

        assertEquals(401, run(new ApiKeyFilter("segredo"), "/api/v1/access-logs", "errada", wrongChain).getStatus());
        assertEquals(401, run(new ApiKeyFilter("segredo"), "/api/v1/access-logs", null, missingChain).getStatus());
        assertNull(wrongChain.getRequest());
        assertNull(missingChain.getRequest());
    }

    @Test
    void blankConfiguredKeyRejectsEverything() throws Exception {
        assertEquals(401, run(new ApiKeyFilter(""), "/api/v1/access-logs", "", new MockFilterChain()).getStatus());
    }

    @Test
    void swaggerIsOpen() throws Exception {
        MockFilterChain chain = new MockFilterChain();

        assertEquals(200, run(new ApiKeyFilter("segredo"), "/swagger-ui/index.html", null, chain).getStatus());
        assertNotNull(chain.getRequest());
    }
}
