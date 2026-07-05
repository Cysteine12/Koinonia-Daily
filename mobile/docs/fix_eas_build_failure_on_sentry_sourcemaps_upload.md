# Fix: EAS Android Build Fails with `sentry-cli` Process Error (Expo + pnpm)

## Error

Android EAS builds failed during the Sentry source map upload step with:

```text
Execution failed for task ':app:createBundleReleaseJsAndAssets_SentryUpload_...'

A problem occurred starting process 'command '.../node_modules/@sentry/cli/bin/sentry-cli''
```

## Environment

- Expo SDK 55 (also reproduced on SDK 54)
- EAS Build
- pnpm
- `@sentry/react-native` installed via `expo install`

## Root Cause

Although `@sentry/react-native` installs `@sentry/cli` as a transitive dependency, the EAS build was unable to execute the CLI binary during the Gradle Sentry upload task.

## Solution

Explicitly install `@sentry/cli` as a direct development dependency:

```bash
pnpm exec expo install @sentry/cli -- -D
```

This resolved the issue immediately, and EAS Android builds completed successfully.

## Notes

The following did **not** resolve the issue:

- Changing Node.js versions
- Upgrading from Expo SDK 54 to 55
- Setting `SENTRY_ALLOW_FAILURE=1`
- Setting `SENTRY_LOG_LEVEL=debug`
- Verifying pnpm build approvals
- Regenerating the lockfile

- Found the fix through searching the expo official discord community.

If you encounter this error while using **Expo + pnpm + Sentry + EAS Build**, ensure `@sentry/cli` is explicitly listed in your `devDependencies`.