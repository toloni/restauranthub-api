package br.com.postechfiap.toloni.restauranthub.domain.menuitem.valueobject;

import br.com.postechfiap.toloni.restauranthub.domain.shared.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class MenuItemPriceTest {

    @Test
    @DisplayName("should create MenuItemPrice in BRL via ofBRL")
    void shouldCreateInBRL() {
        var price = MenuItemPrice.ofBRL(new BigDecimal("12.90"));
        assertThat(price.getAmount()).isEqualByComparingTo(new BigDecimal("12.90"));
        assertThat(price.getCurrency()).isEqualTo(Currency.getInstance("BRL"));
    }

    @Test
    @DisplayName("should create MenuItemPrice in USD via ofUSD")
    void shouldCreateInUSD() {
        var price = MenuItemPrice.ofUSD(new BigDecimal("9.99"));
        assertThat(price.getAmount()).isEqualByComparingTo(new BigDecimal("9.99"));
        assertThat(price.getCurrency()).isEqualTo(Currency.getInstance("USD"));
    }

    @Test
    @DisplayName("should create MenuItemPrice via of with explicit currency")
    void shouldCreateWithExplicitCurrency() {
        var brl = Currency.getInstance("BRL");
        var price = MenuItemPrice.of(new BigDecimal("5.00"), brl);
        assertThat(price.getAmount()).isEqualByComparingTo(new BigDecimal("5.00"));
        assertThat(price.getCurrency()).isEqualTo(brl);
    }

    @Test
    @DisplayName("should scale amount to currency fraction digits")
    void shouldScaleAmount() {
        var price = MenuItemPrice.ofBRL(new BigDecimal("12.9"));
        assertThat(price.getAmount().scale()).isEqualTo(2);
        assertThat(price.getAmount()).isEqualByComparingTo(new BigDecimal("12.90"));
    }

    @Test
    @DisplayName("should accept zero amount")
    void shouldAcceptZeroAmount() {
        var price = MenuItemPrice.ofBRL(BigDecimal.ZERO);
        assertThat(price.getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("should throw DomainException when amount is null")
    void shouldThrowWhenAmountIsNull() {
        assertThatThrownBy(() -> MenuItemPrice.of(null, Currency.getInstance("BRL")))
                .isInstanceOf(DomainException.class)
                .hasMessage("MenuItem price is required.");
    }

    @Test
    @DisplayName("should throw DomainException when currency is null")
    void shouldThrowWhenCurrencyIsNull() {
        assertThatThrownBy(() -> MenuItemPrice.of(new BigDecimal("10.00"), null))
                .isInstanceOf(DomainException.class)
                .hasMessage("MenuItem currency is required.");
    }

    @Test
    @DisplayName("should throw DomainException when amount is negative")
    void shouldThrowWhenAmountIsNegative() {
        assertThatThrownBy(() -> MenuItemPrice.ofBRL(new BigDecimal("-1.00")))
                .isInstanceOf(DomainException.class)
                .hasMessage("MenuItem price must not be negative.");
    }

    @Test
    @DisplayName("should be equal when same amount and currency")
    void shouldBeEqualWhenSameAmountAndCurrency() {
        var a = MenuItemPrice.ofBRL(new BigDecimal("12.90"));
        var b = MenuItemPrice.ofBRL(new BigDecimal("12.90"));
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("should not be equal when different amount")
    void shouldNotBeEqualWhenDifferentAmount() {
        var a = MenuItemPrice.ofBRL(new BigDecimal("12.90"));
        var b = MenuItemPrice.ofBRL(new BigDecimal("15.00"));
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("should not be equal when different currency")
    void shouldNotBeEqualWhenDifferentCurrency() {
        var a = MenuItemPrice.ofBRL(new BigDecimal("12.90"));
        var b = MenuItemPrice.ofUSD(new BigDecimal("12.90"));
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    @DisplayName("toString should include currency symbol and amount")
    void toStringShouldIncludeCurrencyAndAmount() {
        var price = MenuItemPrice.ofBRL(new BigDecimal("12.90"));
        assertThat(price.toString()).contains("12.90");
    }
}
