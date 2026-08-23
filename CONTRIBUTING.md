# Contributing Guidelines

Thank you for contributing to AI Coding Interview Prep.

## How to contribute

1. Clone this repository directly rather than forking it. All team members already have push access, so forking isn't necessary to contribute — and GitHub doesn't pass repository secrets (like `SONAR_TOKEN`) to workflows triggered by pull requests from a fork, so SonarCloud can't run on those PRs (we hit this with PR #14). Forking is still possible if you prefer it, but a fork PR won't get a SonarCloud check — push a branch here directly if you want that signal.
2. Create a branch for your change: `git checkout -b feature-name`.
3. Make small, focused commits.
4. Push your branch to GitHub.
5. Open a pull request describing the change.

## Coding standards

- Use clear and descriptive names.
- Keep code simple and maintainable.
- Add tests for new behavior when appropriate.

## Issue workflow

- Use issue templates for bug reports and feature requests.
- Link pull requests to the related issue when possible.
- Before starting work, check that no one else is already assigned - assign
  yourself (or ask a maintainer to assign you) so effort isn't duplicated.

## Issue approval

New issues (bug reports or feature requests) need team approval before anyone
starts work on them:

- Bring new issues up at the next weekly meeting (Monday in-person or
  Thursday online), or
- If it can't wait for a meeting, approval can come from a comment or
  reaction from at least one other team member.

Either way, record the approval as a comment on the issue itself (e.g. "approved,
go ahead") before work begins, so there's a record of when and how it was approved.

## Code quality

- Use SonarLint in the IDE for in-IDE analysis.
- Ensure SonarCloud analysis passes on `main` branch.
- Keep pull requests small and reviewable.

## Custom Labels
- `A2`: For Features/Work related to the A2.
