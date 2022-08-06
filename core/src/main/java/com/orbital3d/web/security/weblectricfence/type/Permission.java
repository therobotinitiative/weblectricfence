package com.orbital3d.web.security.weblectricfence.type;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Permission class for Weblectric Fence.<br>
 * <p>
 * All permissions are converted into {@link Permission} class and it is used to
 * verify sufficient permission. This class is modelled after Apache Shiro
 * permission and the permission consist of parts separated by ':' delimiter.
 * There is no limit how many there can be. Parts are checked if they match or
 * '*' wild card can be used to match anything.
 * </p>
 * <p>
 * If the permission ends with wild card it is treated as rest of the permission
 * allows anything. Meaning the if the required permission is
 * "perm1:perm2:perm3" and the checked permission is "perm1:*" then it is
 * allowed because only first part (perm1) must match.
 * </p>
 * 
 * @since 0.1
 * @author msiren
 *
 */
public class Permission {
	private static final String PERMISSION_SEPARATOR = ":";
	private static final String WILDCARD = "*";

	private final String value;

	private transient String[] parts;

	// Enforce static factory method usage.
	private Permission(String value) {
		this.value = value;
		this.parts = value.split(PERMISSION_SEPARATOR);
	}

	/**
	 * @return Full permission string
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @see Permission#isPermitted(String)
	 */
	public boolean isPermitted(final Permission value) {
		return isPermitted(value.value);
	}

	/**
	 * Write javadocs.
	 * 
	 * @param value {@link Permission} to check
	 * @return true if the given permission is permitted; otherwise false
	 */
	public boolean isPermitted(final String value) {
		if (value.equals(WILDCARD)) {
			return true;
		}
		return Permission.matchParts(parts, value.split(Permission.PERMISSION_SEPARATOR));
	}

	private static boolean matchParts(String[] parts1, String[] parts2) {
		if (parts2.length > parts1.length) {
			if (!Permission.isLastWildCard(parts1)) {
				return false;
			}
		}
		if (parts1.length > parts2.length) {
			if (!Permission.isLastWildCard(parts2)) {
				return false;
			}
		}
		int shorter = (parts1.length < parts2.length) ? parts1.length : parts2.length;
		for (int i = 0; i < shorter; i++) {
			if (!parts1[i].equalsIgnoreCase(parts2[i]) && !Permission.isWildcard(parts1[i])
					&& !Permission.isWildcard(parts2[i])) {
				return false;
			}
		}
		return true;
	}

	private static boolean isLastWildCard(String[] parts) {
		return parts[parts.length - 1].equals(WILDCARD);
	}

	private static boolean isWildcard(String part) {
		return part.equals(WILDCARD);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(3, 427, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(obj, this, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "[" + value + "]";
	}

	/**
	 * Static factory method.
	 * 
	 * @param permission Permission string
	 * @return New {@link Permission} instance
	 * @throws IllegalArgumentException If the permission string is empty.
	 */
	public static Permission of(final String permission) {
		if (StringUtils.isAllEmpty(permission)) {
			throw new IllegalArgumentException("permission must not be empty");
		}
		return new Permission(permission);
	}

}
