package com.yidigun.utils;

/**
 * A utility class for building hash codes in a deterministic and consistent manner.
 *
 * This class was written following the Rule 09 guidelines of Effective Java, 2nd Edition (Joshua Bloch, 2014).
 *
 * The {@code HashCodeBuilder} simplifies the process of creating robust hash code implementations
 * by combining the hash codes of individual values using a prime number-based algorithm.
 *
 * Thread safety is not guaranteed, and this class should be used in a single-threaded context.
 *
 * Features:
 * - Provides multiple `append` methods to add different types of values (e.g., primitives, Objects, arrays).
 * - Aggregates hash codes using a basic multiplier-and-add pattern with configurable base parameters.
 * - Can calculate a final hash code via the {@link #toHashCode()} method.
 */
public class HashCodeBuilder {

    private static final int INITIAL = 17;
    private static final int BASE = 31;
    private static final int NULL_HASH_CODE = 0;

    private int hashCode;
    private int appendedValuesCount = 0;

    /**
     * Creates a new instance of {@code HashCodeBuilder} with the initial hash code value.
     * This constructor initializes the internal state of the hash code computation.
     * The starting hash code value is set to a predefined constant.
     * This class is designed to simplify and standardize the process of creating hash codes
     * for objects, supporting method chaining and various data types.
     */
    public HashCodeBuilder() {
        hashCode = INITIAL;
    }

    /**
     * Generates a hash code for a variable number of input values using the {@code HashCodeBuilder}.
     * The method combines the hash codes of the given values to produce a consistent and deterministic result.
     *
     * @param values the variable number of input objects to generate the hash code for.
     *               Null values are handled by using a {@code 0} hash code for them.
     * @return the computed hash code as an integer based on the input values.
     */
    public static int toHashCode(Object... values) {
        HashCodeBuilder builder = new HashCodeBuilder();
        for (Object value : values)
            builder.append(value);
        return builder.toHashCode();
    }

    /**
     * Retrieves the computed hash code from the HashCodeBuilder instance.
     * This method returns the final hash code generated based on the values appended
     * using the various append methods in the HashCodeBuilder.
     *
     * @return the computed hash code as an integer.
     */
    public int toHashCode() {
        return hashCode;
    }

    /**
     * Appends an integer value to the current hash code computation.
     * This method modifies the internal hash code state using the provided value.
     *
     * @param value the integer value to be appended to the hash code calculation
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining
     */
    public HashCodeBuilder append(int value) {
        hashCode = BASE * hashCode + value;
        appendedValuesCount++;
        return this;
    }

    /**
     * Appends an array of integer values to the current hash code computation.
     * This method modifies the internal hash code state using the provided values.
     *
     * @param values the array of integer values to be appended to the hash code calculation.
     *               If the array is {@code null} or empty, no modifications are made.
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining.
     */
    public HashCodeBuilder append(int... values) {
        for (int element : values)
            append(element);
        return this;
    }

    /**
     * Appends a boolean value to the current hash code computation.
     * This method modifies the internal hash code state by converting the boolean value
     * to an integer (1 for true, 0 for false) before appending it.
     *
     * @param value the boolean value to be appended to the hash code calculation
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining
     */
    public HashCodeBuilder append(boolean value) {
        return append(value ? 1 : 0);
    }

    /**
     * Appends an array of boolean values to the current hash code computation.
     * This method modifies the internal hash code state by converting each boolean value
     * to an integer (1 for true, 0 for false) before appending it.
     *
     * @param values the array of boolean values to be appended to the hash code calculation.
     *               If the array is {@code null} or empty, no modifications are made.
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining.
     */
    public HashCodeBuilder append(boolean... values) {
        for (boolean value : values)
            append(value ? 1 : 0);
        return this;
    }

    /**
     * Appends a long value to the current hash code computation.
     * This method modifies the internal hash code state by processing the provided long value.
     *
     * @param value the long value to be appended to the hash code calculation
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining
     */
    public HashCodeBuilder append(long value) {
        return append((int)(value ^ (value >>> 32)));
    }

    /**
     * Appends an array of long values to the current hash code computation.
     * This method modifies the internal hash code state by processing each provided long value.
     *
     * @param values the array of long values to be appended to the hash code calculation.
     *               If the array is {@code null} or empty, no modifications are made.
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining.
     */
    public HashCodeBuilder append(long... values) {
        for (long value : values)
            append(value);
        return this;
    }

    /**
     * Appends a float value to the current hash code computation.
     * This method modifies the internal hash code state by converting
     * the provided float value to its integer bit representation before appending it.
     *
     * @param value the float value to be appended to the hash code calculation
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining
     */
    public HashCodeBuilder append(float value) {
        return append(Float.floatToIntBits(value));
    }

    /**
     * Appends an array of float values to the current hash code computation.
     * This method modifies the internal hash code state by converting each float value
     * to its integer bit representation before appending it.
     *
     * @param values the array of float values to be appended to the hash code calculation.
     *               If the array is {@code null} or empty, no modifications are made.
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining.
     */
    public HashCodeBuilder append(float... values) {
        for (float value : values)
            append(Float.floatToIntBits(value));
        return this;
    }

    /**
     * Appends a double value to the current hash code computation.
     * This method modifies the internal hash code state by converting the double value
     * to its equivalent long bit representation before appending it.
     *
     * @param value the double value to be appended to the hash code calculation
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining
     */
    public HashCodeBuilder append(double value) {
        return append(Double.doubleToLongBits(value));
    }

    /**
     * Appends an array of double values to the current hash code computation.
     * This method modifies the internal hash code state by converting each double value
     * to its equivalent long bit representation before appending it.
     *
     * @param values the array of double values to be appended to the hash code calculation.
     *               If the array is {@code null} or empty, no modifications are made.
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining.
     */
    public HashCodeBuilder append(double... values) {
        for (double value : values)
            append(Double.doubleToLongBits(value));
        return this;
    }

    /**
     * Appends an object's hash code to the current hash code computation.
     * This method modifies the internal hash code state by processing the hash
     * code of the provided object. If the object is null, a predefined constant
     * hash code is used.
     *
     * @param value the object whose hash code is to be appended to the hash code calculation.
     *              If the object is null, a default hash code value is appended.
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining.
     */
    public HashCodeBuilder append(Object value) {
        append(value == null ? NULL_HASH_CODE : value.hashCode());
        return this;
    }

    /**
     * Appends the hash codes of a variable number of objects to the current hash code computation.
     * This method iterates over the provided objects and appends each non-null object's hash code.
     * If an object is {@code null}, a predefined hash code constant is used instead.
     *
     * @param values the variable number of objects whose hash codes are to be appended.
     *               Null values are handled by using a predefined constant hash code.
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining.
     */
    public HashCodeBuilder append(Object... values) {
        for (Object value : values)
            append(value == null ? NULL_HASH_CODE : value.hashCode());
        return this;
    }

    /**
     * Appends a byte value to the current hash code computation.
     * This method modifies the internal hash code state by converting
     * the byte value to an integer before appending it.
     *
     * @param value the byte value to be appended to the hash code calculation
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining
     */
    public HashCodeBuilder append(byte value) {
        return append((int)value);
    }

    /**
     * Appends an array of byte values to the current hash code computation.
     * This method modifies the internal hash code state by converting each byte
     * value to an integer before appending it.
     *
     * @param values the array of byte values to be appended to the hash code calculation.
     *               If the array is {@code null}, no modifications are made.
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining.
     */
    public HashCodeBuilder append(byte... values) {
        for (byte value : values)
            append((int)value);
        return this;
    }

    /**
     * Appends a character value to the current hash code computation.
     * This method modifies the internal hash code state by converting
     * the character value to its integer equivalent before appending it.
     *
     * @param value the character value to be appended to the hash code calculation
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining
     */
    public HashCodeBuilder append(char value) {
        return append((int)value);
    }

    /**
     * Appends an array of character values to the current hash code computation.
     * This method modifies the internal hash code state by converting each character
     * value to its integer equivalent before appending it.
     *
     * @param values the array of character values to be appended to the hash code calculation.
     *               If the array is {@code null} or empty, no modifications are made.
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining.
     */
    public HashCodeBuilder append(char... values) {
        for (char value : values) {
            append((int)value);
        }
        return this;
    }

    /**
     * Appends a short value to the current hash code computation.
     * This method modifies the internal hash code state by converting
     * the short value to an integer before appending it.
     *
     * @param value the short value to be appended to the hash code calculation
     * @return the current instance of {@code HashCodeBuilder}, allowing for method chaining
     */
    public HashCodeBuilder append(short value) {
        return append((int)value);
    }

    /**
     * Appends one or more short values to the hash code computation.
     *
     * @param values an array of short values to be added to the hash code calculation
     * @return this HashCodeBuilder instance for method chaining
     */
    public HashCodeBuilder append(short... values) {
        for (short value : values)
            append((int)value);
        return this;
    }
}
