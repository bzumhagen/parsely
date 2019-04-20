#!/bin/sh
# Prepare git repository by setting up a commit template and validation

script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
parent_dir="$( cd ${script_dir}/../ && pwd)"

for f in "$parent_dir/git/hooks"/*; do ln -sf $f "$parent_dir/.git/hooks/$(basename $f)"; done

git config commit.template "git/.gitmessage"
