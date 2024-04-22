package io.github.asvid.mvvmexample.domain.items.usecases

interface ValidateInputUseCase {
    operator fun invoke(input: String?): Result<Int>
}

class ValidateInputUseCaseImpl : ValidateInputUseCase {
    override fun invoke(input: String?): Result<Int> {
        return if (input.isNullOrEmpty()) {
            Result.failure(Exception("input is empty"))
        }
        else if (input.any { !it.isDigit() }) {
            Result.failure(Exception("non-digit detected"))
        }
        else Result.success(input.toInt())
    }

}