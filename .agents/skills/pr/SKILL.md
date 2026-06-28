description = "Generates a Pull Request description using the template. Usage: /pr [base-branch]"

prompt = """
# Task: Generate a Pull Request Description

I need you to generate a Pull Request title and description based on the changes in the current branch.

## Context
- **Current Branch:** !{git branch --show-current}
- **Target Base Branch:** {{args}} (defaults to 'develop' if not specified)
- **PR Template:**
```markdown
!{cat .github/pull_request_template.md}
```

## Instructions
1.  **Identify Base Branch:** Use '{{args}}' as the base branch. If '{{args}}' is empty, use 'develop'.
2.  **Analyze Changes:** Use `run_shell_command` with `git diff <base>...` to analyze the changes introduced in the current branch.
    - If the diff is very large, identify the most important files first and analyze them individually.
3.  **Complete the Template:** Fill out the PR template provided above.
    - Be specific and technical in your descriptions.
    - For 'Type of Change' and 'How was this tested', use [x] to mark the correct checkboxes based on your findings (e.g., check 'Test improvement' if you see new tests, 'Refactor' if it's structural, etc.).
    - If you see any relevant screenshots or API examples that could be included as placeholders, describe them.
4.  **Final Response:** Provide the suggested PR title and the fully populated template in markdown format.
"""
