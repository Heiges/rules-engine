import { useNavigate } from 'react-router-dom'
import './Tile.css'

interface TileProps {
  id: string
  name: string
  description: string
}

export function Tile({ id, name, description }: TileProps) {
  const navigate = useNavigate()

  return (
    <button className="tile" onClick={() => navigate(`/tile/${id}`)}>
      <h2>{name}</h2>
      <p>{description}</p>
    </button>
  )
}
