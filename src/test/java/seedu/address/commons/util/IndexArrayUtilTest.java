package seedu.address.commons.util;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.index.Index;

public class IndexArrayUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void compareIndexArrays_bothNullArrays_throwException() {
        assertExceptionThrown(NullPointerException.class, null, null, Optional.empty());
    }

    @Test
    public void compareIndexArrays_oneNullArray_throwException() {
        assertExceptionThrown(NullPointerException.class, new Index[1], null, Optional.empty());
    }

    @Test
    public void compareIndexArrays_arraysContainsZeroElement_throwsIllegalArgumentException() {
        assertExceptionThrown(IllegalArgumentException.class, new Index[0], new Index[0],
                Optional.of("Both arrays must have at least one Index"));
    }

    @Test
    public void compareIndexArrays_differentLengthArrays_throwsIllegalArgumentException() {
        assertExceptionThrown(IllegalArgumentException.class, new Index[1], new Index[2],
                Optional.of("Both arrays must have same number of Index"));
    }

    private void assertExceptionThrown(Class<? extends Throwable> exceptionClass, Index[] arr1, Index[] arr2,
                                       Optional<String> errorMessage) {
        thrown.expect(exceptionClass);
        errorMessage.ifPresent(message -> thrown.expectMessage(message));
        IndexArrayUtil.compareIndexArrays(arr1, arr2);
    }
}
