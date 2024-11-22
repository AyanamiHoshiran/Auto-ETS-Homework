package cn.ayanamihoshiran.autoetshomework.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicInteger

private val json = Json { ignoreUnknownKeys = true }

// 听后转述答案Json解析器
@Serializable
data class ReproduceAnswer(
    val structure_type: String,
    val info: ReproduceInfo
) {
    companion object {
        fun parse(jsonString: String): ReproduceAnswer {
            return json.decodeFromString(jsonString)
        }
    }
    fun getTheBestAnswer(): String {
        return info.std.last().value
    }
}

@Serializable
data class ReproduceInfo(
    val stid: String,
    val value: String,
    val image: String,
    val audio: String,
    val analyze: String,
    val std: List<Std>,
    val ref: List<String>,
    val keypoint: String,
    val topic: String
)

@Serializable
data class Std(
    val value: String,
    val ai: String,
    val audio: String
)

// 听后回答答案Json生成器
@Serializable
data class ListenAnswer(
    val structure_type: String,
    val info: ListenInfo
) {
    companion object {
        fun parse(jsonString: String): ListenAnswer {
            return json.decodeFromString(jsonString)
        }
    }
    fun getTheBestAnswer(index: AtomicInteger): String {
        return info.question.joinToString("") {
            if (info.question.size > 1 && it != info.question.first()) {
                "\n${index.incrementAndGet()}: ${it.std[2].value}"
            } else {
                 it.std[2].value
            }
        }
    }
}

@Serializable
data class ListenInfo(
    val question: List<ListenQuestion>,
    val image: String,
    val video: String,
    val value: String,
    val stid: String,
    val audio: String
)

@Serializable
data class ListenQuestion(
    val std: List<Std>,
    val ref: List<String>,
    val ask: String,
    val answer: String,
    val askaudio: String,
    val aswaudio: String,
    val xh: String,
    val analyze: String,
    val sucai: String,
    val keywords: String,
    val role: String
)


// 听后选择Json生成器
@Serializable
data class ChooseAnswer(
    val structure_type: String,
    val info: ChooseInfo
) {
    companion object {
        fun parse(jsonString: String): ChooseAnswer {
            return json.decodeFromString(jsonString)
        }
    }
    fun getAllAnswers(): String {
        return info.xtlist.joinToString(", ") { it.answer }
    }
}

@Serializable
data class ChooseInfo(
    val stid: String,
    val st_sm: String,
    val st_nr: String,
    val audio: String,
    val st_pic: String,
    val xtlist: List<ChooseQuestion>
)

@Serializable
data class ChooseQuestion(
    val xt_xh: String,
    val xt_nr: String,
    val xt_wj: String,
    val xt_wj2: String? = null,
    val xt_value: String,
    val xt_analy: String,
    val xt_pic: String,
    val answer: String,
    val xxlist: List<Option>
)

@Serializable
data class Option(
    val xx_xh: String,
    val xx_mc: String,
    val xx_nr: String,
    val xx_wj: String
)