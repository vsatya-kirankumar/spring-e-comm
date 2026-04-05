package com.ecommerce.project.exception;

import jakarta.validation.GroupSequence;

@GroupSequence({NotBlankGroup.class, SizeGroup.class})
public interface ValidationSequence {
}
