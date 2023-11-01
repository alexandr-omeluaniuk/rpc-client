package ss.rpc

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

@Hidden
@RestController
@RequestMapping("/swagger-ui")
class SwaggerController {

    @GetMapping(path = ["/index.html"], produces = ["text/html"])
    fun index(): String {
        return toText(javaClass.getResourceAsStream("/index.html"))
    }

    fun toText(`in`: InputStream?): String {
        return BufferedReader(InputStreamReader(`in`, StandardCharsets.UTF_8))
            .lines().collect(Collectors.joining("\n"))
    }
}