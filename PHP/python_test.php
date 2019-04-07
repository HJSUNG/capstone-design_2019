<?php
    $command = escapeshellcmd('python3 python_test.py');
    $output = shell_exec($command);
    echo $output;
?>