package cn.ayanamihoshiran.autoetshomework.globalUtils.config;

import cn.ayanamihoshiran.autoetshomework.annotation.ConfigEntity;
import lombok.*;

import java.util.ArrayList;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

@ConfigEntity(name = "Config", filePath = "config")
public class Config {

    /**
     * 记录录音按钮坐标
     * <br>
     * 格式：<code>[x,y]</code>
     */
    private ArrayList<Double> record_coordinates;

}
