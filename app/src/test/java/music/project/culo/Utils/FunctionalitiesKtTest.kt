package music.project.culo.Utils

import com.google.common.truth.Truth
import org.junit.Test

class FunctionalitiesKtTest{


    @Test
    fun `return hour format if is 3600000`(){
        //arrange

        //act
        val result = convertSecondsToHMmSs(3600000)

        //assert
        Truth.assertThat(result).isEqualTo("1:00:00")

    }
}