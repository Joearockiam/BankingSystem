package com.util.input;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.common.Constants;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InputTransUtilTest {

    @Test
    @Order(1)
    void testGetNextTxnIdValidTransactionId() {
        String curTxnId = "20250112-01";
        String expectedNextTxnId = "20250112-02";

        String actualNextTxnId = InputTransUtil.getNextTxnId(curTxnId);

        assertEquals(expectedNextTxnId, actualNextTxnId);
    }

    @Test
    @Order(2)
    void testGetNextTxnIdNullTransactionId() {
        String curTxnId = null;

        String actualNextTxnId = InputTransUtil.getNextTxnId(curTxnId);

        assertNull(actualNextTxnId);
    }

    @Test
    @Order(3)
    void testGetNextTxnIdSequenceLessThanMax() {
        String curTxnId = "20250112-98";
        String expectedNextTxnId = "20250112-99";

        String actualNextTxnId = InputTransUtil.getNextTxnId(curTxnId);

        assertEquals(expectedNextTxnId, actualNextTxnId);
    }

    @Test
    @Order(4)
    void testGetNextTxnIdSequenceExceedsMax() {
        String curTxnId = "20250112-99"; // Assume MAX_TXN_SEQ_NO = 99

        String actualNextTxnId = InputTransUtil.getNextTxnId(curTxnId);

        assertNull(actualNextTxnId);
    }

    @Test
    @Order(5)
    void testGetNextTxnIdLeadingZerosInSequence() {
        String curTxnId = "20250112-09";
        String expectedNextTxnId = "20250112-10";

        String actualNextTxnId = InputTransUtil.getNextTxnId(curTxnId);

        assertEquals(expectedNextTxnId, actualNextTxnId);
    }
}