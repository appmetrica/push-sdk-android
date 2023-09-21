#!/bin/bash

set -e

message="$1"
branch_prefix="$2"

branch_uid=$(tr -cd 'a-zA-Z0-9' </dev/urandom | fold -w 5 | head -n 1)
arc checkout -b "${branch_prefix}-${branch_uid}"
arc commit -m "$message" --all
pr_url="$(arc pr create -m "$message" --no-commits --publish --push --json | sed -n 's/.*"url":"\(.*\)".*/\1/p')"
echo "PR: $pr_url"
