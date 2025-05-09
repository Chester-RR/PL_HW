import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// 模擬收款帳戶，用於驗證訂閱費用支付
public class FakePaymentAccount {
    private double balance; // 帳戶餘額

    public FakePaymentAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    /**
     * 模擬刷卡扣款
     * @param cardNumber 信用卡號
     * @param amount 扣款金額
     * @return 是否成功扣款
     */
    public boolean chargeCard(String cardNumber, double amount) {
        // 簡單驗證：卡號需是 16 位數字
        if (cardNumber == null || !cardNumber.matches("\\\\d{16}")) {
            return false;
        }
        // 模擬扣款：如果帳戶餘額足夠，扣款並返回成功
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public double getBalance() {
        return balance;
    }
}
