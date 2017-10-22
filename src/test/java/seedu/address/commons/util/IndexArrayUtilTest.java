package seedu.address.commons.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

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
    public void compareIndexArrays_arraysContainsZeroElement_validComparison() {
        assertTrue(assertComparison(new Index[0], new Index[0]));
    }

    @Test
    public void compareIndexArrays_differentLengthArrays_invalidComparison() {
        assertFalse(assertComparison(new Index[0], new Index[1]));
    }

    @Test
    public void compareIndexArrays_sameLengthArraysDiffElements_invalidComparison() {
        Index[] arr1 = {INDEX_FIRST_PERSON};
        Index[] arr2 = {INDEX_SECOND_PERSON};
        assertFalse(assertComparison(arr1, arr2));
    }

    @Test
    public void compareIndexArrays_sameLengthSameElements_validComparsion() {
        Index[] arr1 = {INDEX_FIRST_PERSON};
        Index[] arr2 = {INDEX_FIRST_PERSON};
        assertTrue(assertComparison(arr1, arr2));
    }

    private void assertExceptionThrown(Class<? extends Throwable> exceptionClass, Index[] arr1, Index[] arr2,
                                       Optional<String> errorMessage) {
        thrown.expect(exceptionClass);
        errorMessage.ifPresent(message -> thrown.expectMessage(message));
        IndexArrayUtil.compareIndexArrays(arr1, arr2);
    }

    private boolean assertComparison(Index[] arr1, Index[] arr2) {
        return IndexArrayUtil.compareIndexArrays(arr1, arr2);
    }
}
