package com.github.tomjankes.wiremock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject

import static com.github.tomjankes.wiremock.testutils.HttpHelper.getJson

class StubbingSpec extends Specification {

    public static final int PORT = 8080

    @Rule
    WireMockRule wireMockRule = new WireMockRule(PORT)
    @Subject
    def wireMockGroovy = WireMockGroovy.builder().withPort(PORT).enableJsonBodyFeature().build()

    def "should correctly stub wire mock get request" () {
        when:
        wireMockGroovy.stub {
            request {
                method "GET"
                url "/some/thing"
            }
            response {
                status 200
                body "Some body"
                headers {
                    "Content-Type" "text/plain"
                }
            }
        }

        then:
        new URL("http://localhost:8080/some/thing").getText() == "Some body"
    }

    def "should automagically handle json" () {
        when:
        wireMockGroovy.stub {
            request {
                method "GET"
                url "/some/thing"
            }
            response {
                status 200
                jsonBody {
                    element "elementValue"
                }
                headers {
                    "Content-Type" "text/plain"
                }
            }
        }

        then:
        getJson("http://localhost:8080/some/thing").element == "elementValue"
    }
}
