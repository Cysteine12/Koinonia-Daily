description = "Summarizes the current session, creates a context file in /.agent/context/, and calls /compress"
prompt = """
Please summarize this session, focusing on:
1. **Context & Objective:** What was the initial goal of this session?
2. **Key Decisions & Findings:** What architectural or implementation decisions were made? What was discovered during the research phase?
3. **Completed Tasks:** List the changes that were implemented (e.g., endpoints documented, DTOs analyzed).
4. **Current Status:** What is the current state of the workspace?
5. **Next Steps:** What tasks remain or should be prioritized in the next session?

After generating the summary:
1. Take the raw value `{{args}}`, and derive a safe slug `SLUG` by keeping only `[A-Za-z0-9_-]` no slashes/dots/spaces or any other characters
2. Use the `write_file` tool to save it to `.agent/context/session_summary_<SLUG>.md`.  
    (substitute `<SLUG>` with the sanitized value you just computed. Do not reuse the raw `{{args}}` in the path)
3. The filename should be descriptive (e.g., '2026-04-14_api-docs-update').

Finally, after saving the file, call the `/compress` command to optimize the session history.
"""
