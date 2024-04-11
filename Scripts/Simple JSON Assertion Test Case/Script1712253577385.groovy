import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.json

assertThatJson('[1,2,3]').isEqualTo('[1,2,4]')
