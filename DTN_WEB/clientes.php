<?php

include_once './conexao.php';

header('Content-type:application/json');
$lista = array();
$query = mysqli_query($con, "SELECT * FROM cliente_rede ORDER BY nome,telefone");
while ($l = mysqli_fetch_array($query)) {
    array_push($lista, $l);
}
echo json_encode($lista);
